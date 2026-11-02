package com.nexa.island

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.nexa.island.core.permissions.NexaPermissionManager
import com.nexa.island.feature.dashboard.ui.DashboardScreen
import com.nexa.island.feature.dashboard.viewmodel.DashboardViewModel
import com.nexa.island.service.IslandOverlayService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var permissionManager: NexaPermissionManager

    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        tryStartOverlayService()

        setContent {
            DashboardScreen(
                viewModel = dashboardViewModel,
                onRequestOverlayPermission = {
                    startActivity(permissionManager.createOverlaySettingsIntent())
                },
                onRequestNotificationPermission = {
                    startActivity(permissionManager.createNotificationListenerSettingsIntent())
                },
                onOpenMiuiSettings = {
                    startActivity(permissionManager.createMiuiAutostartIntent())
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        dashboardViewModel.refreshPermissions()
        tryStartOverlayService()
    }

    private fun tryStartOverlayService() {
        if (permissionManager.hasOverlayPermission()) {
            val serviceIntent = Intent(this, IslandOverlayService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
        }
    }
}
