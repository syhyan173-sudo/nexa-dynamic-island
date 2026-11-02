package com.nexa.island.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexa.island.core.datastore.IslandPreferencesDataSource
import com.nexa.island.core.datastore.IslandSettings
import com.nexa.island.core.permissions.NexaPermissionManager
import com.nexa.island.core.permissions.PermissionStatus
import com.nexa.island.domain.model.ActiveIslandSession
import com.nexa.island.domain.model.EventPriority
import com.nexa.island.domain.model.IslandEvent
import com.nexa.island.domain.model.IslandExpansionState
import com.nexa.island.domain.repository.IslandEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val session: ActiveIslandSession = ActiveIslandSession(),
    val settings: IslandSettings = IslandSettings(),
    val permissions: PermissionStatus = PermissionStatus(
        hasOverlayPermission = false,
        hasNotificationListenerPermission = false,
        hasUsageStatsPermission = false,
        hasBluetoothPermission = false,
        hasPostNotificationsPermission = false
    )
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val islandRepository: IslandEventRepository,
    private val preferencesDataSource: IslandPreferencesDataSource,
    private val permissionManager: NexaPermissionManager
) : ViewModel() {

    private val _permissions = MutableStateFlow(permissionManager.checkPermissionStatus())

    val uiState: StateFlow<DashboardUiState> = combine(
        islandRepository.sessionState,
        preferencesDataSource.islandSettingsFlow,
        _permissions
    ) { session, settings, permissions ->
        DashboardUiState(
            session = session,
            settings = settings,
            permissions = permissions
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState()
    )

    fun refreshPermissions() {
        _permissions.value = permissionManager.checkPermissionStatus()
    }

    fun toggleIslandEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesDataSource.updateIslandEnabled(enabled)
        }
    }

    fun toggleMusicEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesDataSource.updateMusicEnabled(enabled)
        }
    }

    fun toggleNotificationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesDataSource.updateNotificationEnabled(enabled)
        }
    }

    fun toggleSystemInfoEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesDataSource.updateSystemInfoEnabled(enabled)
        }
    }

    fun toggleGamingModeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesDataSource.updateGamingModeEnabled(enabled)
        }
    }

    fun updateIslandDimensions(widthDp: Int, heightDp: Int, offsetYDp: Int) {
        viewModelScope.launch {
            preferencesDataSource.updateIslandDimensions(widthDp, heightDp, offsetYDp)
        }
    }

    fun updateAccentColor(hex: String) {
        viewModelScope.launch {
            preferencesDataSource.updateAccentColor(hex)
        }
    }

    // Interactive Preview Simulator Actions
    fun testMusicEvent() {
        islandRepository.postEvent(
            IslandEvent.Music(
                id = "demo_music",
                trackTitle = "Starboy",
                artist = "The Weeknd, Daft Punk",
                isPlaying = true,
                durationMs = 230000L,
                positionMs = 84000L,
                packageName = "com.spotify.music"
            )
        )
    }

    fun testNotificationEvent() {
        islandRepository.postEvent(
            IslandEvent.Notification(
                id = "demo_notif_${System.currentTimeMillis()}",
                packageName = "com.whatsapp",
                appName = "WhatsApp",
                title = "Sarah Jenkins",
                message = "Hey! Did you check out the new Nexa Island update? It looks amazing on MIUI 13! 🔥",
                isSensitive = false
            )
        )
    }

    fun testBatteryAlert() {
        islandRepository.postEvent(
            IslandEvent.BatteryAlert(
                id = "demo_battery",
                percentage = 85,
                isCharging = true,
                temperatureCelsius = 31.4f,
                isLowBattery = false
            )
        )
    }

    fun testIncomingCall() {
        islandRepository.postEvent(
            IslandEvent.IncomingCall(
                id = "demo_call",
                callerName = "Alex Rivera",
                callerNumber = "+62 812-3456-7890"
            )
        )
    }

    fun toggleExpansion() {
        islandRepository.toggleExpansion()
    }

    fun dismissEvent(id: String) {
        islandRepository.dismissEvent(id)
    }

    fun clearAllEvents() {
        islandRepository.clearAll()
    }
}
