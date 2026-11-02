package com.nexa.island.data.collector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.nexa.island.domain.model.IslandEvent
import com.nexa.island.domain.repository.IslandEventRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryCollector @Inject constructor(
    @ApplicationContext private val context: Context,
    private val islandRepository: IslandEventRepository
) {
    private var isRegistered = false

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action != Intent.ACTION_BATTERY_CHANGED) return

            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10f

            val percentage = if (level >= 0 && scale > 0) {
                (level * 100) / scale
            } else {
                level
            }

            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL
            val isLowBattery = percentage in 1..20 && !isCharging

            // Post battery alert event
            islandRepository.postEvent(
                IslandEvent.BatteryAlert(
                    id = "battery_status",
                    percentage = percentage,
                    isCharging = isCharging,
                    temperatureCelsius = temperature,
                    isLowBattery = isLowBattery
                )
            )
        }
    }

    fun startListening() {
        if (!isRegistered) {
            val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            context.registerReceiver(batteryReceiver, filter)
            isRegistered = true
        }
    }

    fun stopListening() {
        if (isRegistered) {
            try {
                context.unregisterReceiver(batteryReceiver)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isRegistered = false
        }
    }
}
