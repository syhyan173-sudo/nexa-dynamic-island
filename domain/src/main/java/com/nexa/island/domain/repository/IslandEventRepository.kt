package com.nexa.island.domain.repository

import com.nexa.island.domain.model.ActiveIslandSession
import com.nexa.island.domain.model.IslandEvent
import com.nexa.island.domain.model.IslandExpansionState
import kotlinx.coroutines.flow.StateFlow

interface IslandEventRepository {
    val sessionState: StateFlow<ActiveIslandSession>

    fun postEvent(event: IslandEvent)
    fun dismissEvent(eventId: String)
    fun setExpansionState(state: IslandExpansionState)
    fun toggleExpansion()
    fun clearAll()
}
