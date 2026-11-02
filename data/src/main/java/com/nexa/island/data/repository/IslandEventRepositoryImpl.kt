package com.nexa.island.data.repository

import com.nexa.island.core.common.dispatcher.Dispatcher
import com.nexa.island.core.common.dispatcher.NexaDispatchers
import com.nexa.island.domain.model.ActiveIslandSession
import com.nexa.island.domain.model.IslandEvent
import com.nexa.island.domain.model.IslandExpansionState
import com.nexa.island.domain.repository.IslandEventRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IslandEventRepositoryImpl @Inject constructor(
    @Dispatcher(NexaDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher
) : IslandEventRepository {

    private val repositoryScope = CoroutineScope(defaultDispatcher + SupervisorJob())

    private val _sessionState = MutableStateFlow(ActiveIslandSession())
    override val sessionState: StateFlow<ActiveIslandSession> = _sessionState.asStateFlow()

    private val activeEventQueue = mutableListOf<IslandEvent>()

    override fun postEvent(event: IslandEvent) {
        repositoryScope.launch {
            synchronized(activeEventQueue) {
                // Remove older instance of same id if present
                activeEventQueue.removeAll { it.id == event.id }
                activeEventQueue.add(event)

                // Sort by priority descending (highest level first), then by timestamp descending
                activeEventQueue.sortWith(
                    compareByDescending<IslandEvent> { it.priority.level }
                        .thenByDescending { it.timestamp }
                )

                val highestPriorityEvent = activeEventQueue.firstOrNull()

                _sessionState.update { current ->
                    current.copy(
                        currentEvent = highestPriorityEvent,
                        eventQueue = activeEventQueue.toList(),
                        // Reset to compact when a new higher event takes over
                        expansionState = if (current.currentEvent?.id != highestPriorityEvent?.id) {
                            IslandExpansionState.COMPACT
                        } else {
                            current.expansionState
                        }
                    )
                }
            }
        }
    }

    override fun dismissEvent(eventId: String) {
        repositoryScope.launch {
            synchronized(activeEventQueue) {
                activeEventQueue.removeAll { it.id == eventId }
                val nextEvent = activeEventQueue.firstOrNull()

                _sessionState.update { current ->
                    current.copy(
                        currentEvent = nextEvent,
                        eventQueue = activeEventQueue.toList(),
                        expansionState = IslandExpansionState.COMPACT
                    )
                }
            }
        }
    }

    override fun setExpansionState(state: IslandExpansionState) {
        _sessionState.update { it.copy(expansionState = state) }
    }

    override fun toggleExpansion() {
        _sessionState.update { current ->
            val newState = when (current.expansionState) {
                IslandExpansionState.COMPACT -> IslandExpansionState.EXPANDED
                IslandExpansionState.EXPANDED -> IslandExpansionState.COMPACT
                IslandExpansionState.HIDDEN -> IslandExpansionState.COMPACT
            }
            current.copy(expansionState = newState)
        }
    }

    override fun clearAll() {
        repositoryScope.launch {
            synchronized(activeEventQueue) {
                activeEventQueue.clear()
            }
            _sessionState.update {
                ActiveIslandSession(
                    currentEvent = null,
                    expansionState = IslandExpansionState.COMPACT,
                    eventQueue = emptyList()
                )
            }
        }
    }
}
