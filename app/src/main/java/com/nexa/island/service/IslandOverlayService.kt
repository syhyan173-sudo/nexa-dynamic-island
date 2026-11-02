package com.nexa.island.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.nexa.island.MainActivity
import com.nexa.island.NexaApplication
import com.nexa.island.R
import com.nexa.island.core.datastore.IslandPreferencesDataSource
import com.nexa.island.core.datastore.IslandSettings
import com.nexa.island.core.overlay.OverlayManager
import com.nexa.island.data.collector.BatteryCollector
import com.nexa.island.domain.model.ActiveIslandSession
import com.nexa.island.domain.model.IslandExpansionState
import com.nexa.island.domain.repository.IslandEventRepository
import com.nexa.island.feature.island.ui.DynamicIslandOverlayView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IslandOverlayService : Service(), LifecycleOwner, SavedStateRegistryOwner {

    @Inject
    lateinit var overlayManager: OverlayManager

    @Inject
    lateinit var islandRepository: IslandEventRepository

    @Inject
    lateinit var preferencesDataSource: IslandPreferencesDataSource

    @Inject
    lateinit var batteryCollector: BatteryCollector

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    private var composeView: ComposeView? = null

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        startForegroundService()
        batteryCollector.startListening()
        initOverlay()
    }

    private fun startForegroundService() {
        val openIntent = Intent(this, MainActivity::class.java).let {
            PendingIntent.getActivity(
                this,
                0,
                it,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val notification: Notification = NotificationCompat.Builder(this, NexaApplication.OVERLAY_SERVICE_CHANNEL_ID)
            .setContentTitle("Nexa Island is active")
            .setContentText("Overlay engine running smoothly")
            .setSmallIcon(R.drawable.ic_nexa_island)
            .setContentIntent(openIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun initOverlay() {
        composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@IslandOverlayService)
            setViewTreeSavedStateRegistryOwner(this@IslandOverlayService)
        }

        val density = resources.displayMetrics.density

        serviceScope.launch {
            combine(
                islandRepository.sessionState,
                preferencesDataSource.islandSettingsFlow
            ) { session, settings ->
                Pair(session, settings)
            }.collect { (session, settings) ->
                updateOverlayWindow(session, settings, density)
            }
        }
    }

    private fun updateOverlayWindow(
        session: ActiveIslandSession,
        settings: IslandSettings,
        density: Float
    ) {
        val view = composeView ?: return

        if (!settings.isIslandEnabled || session.expansionState == IslandExpansionState.HIDDEN) {
            overlayManager.detachOverlay()
            return
        }

        val widthPx = when (session.expansionState) {
            IslandExpansionState.COMPACT -> (settings.islandWidthDp * density).toInt()
            IslandExpansionState.EXPANDED -> (resources.displayMetrics.widthPixels * 0.94f).toInt()
            IslandExpansionState.HIDDEN -> 0
        }

        val heightPx = when (session.expansionState) {
            IslandExpansionState.COMPACT -> (settings.islandHeightDp * density).toInt()
            IslandExpansionState.EXPANDED -> (180 * density).toInt()
            IslandExpansionState.HIDDEN -> 0
        }

        val offsetYPx = (settings.islandOffsetYDp * density).toInt()
        val params = overlayManager.createLayoutParams(widthPx, heightPx, offsetYPx)

        view.setContent {
            DynamicIslandOverlayView(
                session = session,
                settings = settings,
                onToggleExpansion = { islandRepository.toggleExpansion() },
                onDismissEvent = { islandRepository.dismissEvent(it) }
            )
        }

        if (!overlayManager.isOverlayAttached()) {
            overlayManager.attachOverlay(view, params)
        } else {
            overlayManager.updateOverlay(params)
        }
    }

    override fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        batteryCollector.stopListening()
        overlayManager.detachOverlay()
        composeView = null
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val NOTIFICATION_ID = 1001
    }
}
