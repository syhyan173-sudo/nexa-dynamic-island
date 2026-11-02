package com.nexa.island.data.collector

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.nexa.island.domain.model.IslandEvent
import com.nexa.island.domain.repository.IslandEventRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationCollectorService : NotificationListenerService() {

    @Inject
    lateinit var islandRepository: IslandEventRepository

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn ?: return

        // Filter out ongoing foreground notifications or system alarms to avoid spam
        val notification = sbn.notification ?: return
        val isOngoing = (notification.flags and Notification.FLAG_ONGOING_EVENT) != 0
        if (isOngoing && !isMediaNotification(notification)) {
            return
        }

        val packageName = sbn.packageName ?: return
        if (packageName == applicationContext.packageName) {
            return
        }

        val extras = notification.extras ?: return
        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString() ?: ""
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""

        if (title.isBlank() && text.isBlank()) return

        val appName = try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            packageName
        }

        val event = IslandEvent.Notification(
            id = "notif_${sbn.id}_$packageName",
            packageName = packageName,
            appName = appName,
            title = title,
            message = text,
            isSensitive = false
        )

        islandRepository.postEvent(event)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        sbn ?: return
        islandRepository.dismissEvent("notif_${sbn.id}_${sbn.packageName}")
    }

    private fun isMediaNotification(notification: Notification): Boolean {
        val style = notification.extras?.getString(Notification.EXTRA_TEMPLATE) ?: ""
        return style.contains("MediaStyle")
    }
}
