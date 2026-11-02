package com.nexa.island.domain.model

enum class EventPriority(val level: Int) {
    INCOMING_CALL(100),
    NAVIGATION(90),
    NOTIFICATION(80),
    MUSIC(60),
    BATTERY_ALERT(50),
    SYSTEM_STATUS(20),
    IDLE(0)
}

sealed interface IslandEvent {
    val id: String
    val priority: EventPriority
    val timestamp: Long

    data class IncomingCall(
        override val id: String,
        val callerName: String,
        val callerNumber: String,
        val hasVideo: Boolean = false,
        override val priority: EventPriority = EventPriority.INCOMING_CALL,
        override val timestamp: Long = System.currentTimeMillis()
    ) : IslandEvent

    data class Navigation(
        override val id: String,
        val maneuverDescription: String,
        val distanceText: String,
        val etaMinutes: Int,
        override val priority: EventPriority = EventPriority.NAVIGATION,
        override val timestamp: Long = System.currentTimeMillis()
    ) : IslandEvent

    data class Notification(
        override val id: String,
        val packageName: String,
        val appName: String,
        val title: String,
        val message: String,
        val isSensitive: Boolean = false,
        override val priority: EventPriority = EventPriority.NOTIFICATION,
        override val timestamp: Long = System.currentTimeMillis()
    ) : IslandEvent

    data class Music(
        override val id: String,
        val trackTitle: String,
        val artist: String,
        val isPlaying: Boolean,
        val durationMs: Long,
        val positionMs: Long,
        val packageName: String,
        override val priority: EventPriority = EventPriority.MUSIC,
        override val timestamp: Long = System.currentTimeMillis()
    ) : IslandEvent

    data class BatteryAlert(
        override val id: String,
        val percentage: Int,
        val isCharging: Boolean,
        val temperatureCelsius: Float? = null,
        val isLowBattery: Boolean = false,
        override val priority: EventPriority = EventPriority.BATTERY_ALERT,
        override val timestamp: Long = System.currentTimeMillis()
    ) : IslandEvent

    data class SystemStatus(
        override val id: String,
        val statusTitle: String,
        val statusSubtitle: String,
        val iconType: String,
        override val priority: EventPriority = EventPriority.SYSTEM_STATUS,
        override val timestamp: Long = System.currentTimeMillis()
    ) : IslandEvent

    data class GamingHud(
        override val id: String,
        val gamePackageName: String,
        val gameTitle: String,
        val fpsEstimate: Int = 60,
        override val priority: EventPriority = EventPriority.SYSTEM_STATUS,
        override val timestamp: Long = System.currentTimeMillis()
    ) : IslandEvent

    data class Timer(
        override val id: String,
        val label: String,
        val remainingSeconds: Int,
        val totalSeconds: Int,
        val isRunning: Boolean,
        override val priority: EventPriority = EventPriority.SYSTEM_STATUS,
        override val timestamp: Long = System.currentTimeMillis()
    ) : IslandEvent
}

enum class IslandExpansionState {
    COMPACT,
    EXPANDED,
    HIDDEN
}

data class ActiveIslandSession(
    val currentEvent: IslandEvent? = null,
    val expansionState: IslandExpansionState = IslandExpansionState.COMPACT,
    val isLandscape: Boolean = false,
    val isGamingActive: Boolean = false,
    val eventQueue: List<IslandEvent> = emptyList()
)
