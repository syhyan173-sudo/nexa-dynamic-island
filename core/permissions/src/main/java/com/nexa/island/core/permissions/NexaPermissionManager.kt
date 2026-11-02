package com.nexa.island.core.permissions

import android.Manifest
import android.app.AppOpsManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

data class PermissionStatus(
    val hasOverlayPermission: Boolean,
    val hasNotificationListenerPermission: Boolean,
    val hasUsageStatsPermission: Boolean,
    val hasBluetoothPermission: Boolean,
    val hasPostNotificationsPermission: Boolean
) {
    val isCoreReady: Boolean
        get() = hasOverlayPermission
}

@Singleton
class NexaPermissionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun checkPermissionStatus(): PermissionStatus {
        return PermissionStatus(
            hasOverlayPermission = hasOverlayPermission(),
            hasNotificationListenerPermission = hasNotificationListenerPermission(),
            hasUsageStatsPermission = hasUsageStatsPermission(),
            hasBluetoothPermission = hasBluetoothPermission(),
            hasPostNotificationsPermission = hasPostNotificationsPermission()
        )
    }

    fun hasOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(context)
    }

    fun hasNotificationListenerPermission(): Boolean {
        val enabledPackages = NotificationManagerCompat.getEnabledListenerPackages(context)
        return enabledPackages.contains(context.packageName)
    }

    fun hasUsageStatsPermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager ?: return false
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun hasBluetoothPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun hasPostNotificationsPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    // Intent openers with fallback
    fun createOverlaySettingsIntent(): Intent {
        return Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    fun createNotificationListenerSettingsIntent(): Intent {
        return Intent(
            Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    fun createUsageStatsSettingsIntent(): Intent {
        return Intent(
            Settings.ACTION_USAGE_ACCESS_SETTINGS
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * Attempts to open MIUI 13 Autostart Management.
     * Falls back to standard application details if MIUI intent is rejected.
     */
    fun createMiuiAutostartIntent(): Intent {
        val miuiIntent = Intent().apply {
            component = ComponentName(
                "com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity"
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return if (isIntentAvailable(miuiIntent)) {
            miuiIntent
        } else {
            createAppDetailsIntent()
        }
    }

    fun createAppDetailsIntent(): Intent {
        return Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    private fun isIntentAvailable(intent: Intent): Boolean {
        val resolveInfo = context.packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        return resolveInfo.isNotEmpty()
    }
}
