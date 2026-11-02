package com.nexa.island.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "nexa_island_settings")

data class IslandSettings(
    val isIslandEnabled: Boolean = true,
    val isMusicEnabled: Boolean = true,
    val isNotificationEnabled: Boolean = true,
    val isSystemInfoEnabled: Boolean = true,
    val isGamingModeEnabled: Boolean = true,
    val islandWidthDp: Int = 190,
    val islandHeightDp: Int = 38,
    val islandOffsetYDp: Int = 12,
    val cornerRadiusDp: Int = 22,
    val accentColorHex: String = "#A855F7",
    val autoHideTimeoutMs: Long = 4500L
)

@Singleton
class IslandPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val IS_ISLAND_ENABLED = booleanPreferencesKey("is_island_enabled")
        val IS_MUSIC_ENABLED = booleanPreferencesKey("is_music_enabled")
        val IS_NOTIFICATION_ENABLED = booleanPreferencesKey("is_notification_enabled")
        val IS_SYSTEM_INFO_ENABLED = booleanPreferencesKey("is_system_info_enabled")
        val IS_GAMING_MODE_ENABLED = booleanPreferencesKey("is_gaming_mode_enabled")
        val ISLAND_WIDTH_DP = intPreferencesKey("island_width_dp")
        val ISLAND_HEIGHT_DP = intPreferencesKey("island_height_dp")
        val ISLAND_OFFSET_Y_DP = intPreferencesKey("island_offset_y_dp")
        val CORNER_RADIUS_DP = intPreferencesKey("corner_radius_dp")
        val ACCENT_COLOR_HEX = stringPreferencesKey("accent_color_hex")
        val AUTO_HIDE_TIMEOUT_MS = longPreferencesKey("auto_hide_timeout_ms")
    }

    val islandSettingsFlow: Flow<IslandSettings> = context.dataStore.data.map { preferences ->
        IslandSettings(
            isIslandEnabled = preferences[PreferencesKeys.IS_ISLAND_ENABLED] ?: true,
            isMusicEnabled = preferences[PreferencesKeys.IS_MUSIC_ENABLED] ?: true,
            isNotificationEnabled = preferences[PreferencesKeys.IS_NOTIFICATION_ENABLED] ?: true,
            isSystemInfoEnabled = preferences[PreferencesKeys.IS_SYSTEM_INFO_ENABLED] ?: true,
            isGamingModeEnabled = preferences[PreferencesKeys.IS_GAMING_MODE_ENABLED] ?: true,
            islandWidthDp = preferences[PreferencesKeys.ISLAND_WIDTH_DP] ?: 190,
            islandHeightDp = preferences[PreferencesKeys.ISLAND_HEIGHT_DP] ?: 38,
            islandOffsetYDp = preferences[PreferencesKeys.ISLAND_OFFSET_Y_DP] ?: 12,
            cornerRadiusDp = preferences[PreferencesKeys.CORNER_RADIUS_DP] ?: 22,
            accentColorHex = preferences[PreferencesKeys.ACCENT_COLOR_HEX] ?: "#A855F7",
            autoHideTimeoutMs = preferences[PreferencesKeys.AUTO_HIDE_TIMEOUT_MS] ?: 4500L
        )
    }

    suspend fun updateIslandEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.IS_ISLAND_ENABLED] = enabled }
    }

    suspend fun updateMusicEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.IS_MUSIC_ENABLED] = enabled }
    }

    suspend fun updateNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.IS_NOTIFICATION_ENABLED] = enabled }
    }

    suspend fun updateSystemInfoEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.IS_SYSTEM_INFO_ENABLED] = enabled }
    }

    suspend fun updateGamingModeEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.IS_GAMING_MODE_ENABLED] = enabled }
    }

    suspend fun updateIslandDimensions(widthDp: Int, heightDp: Int, offsetYDp: Int) {
        context.dataStore.edit {
            it[PreferencesKeys.ISLAND_WIDTH_DP] = widthDp
            it[PreferencesKeys.ISLAND_HEIGHT_DP] = heightDp
            it[PreferencesKeys.ISLAND_OFFSET_Y_DP] = offsetYDp
        }
    }

    suspend fun updateAccentColor(colorHex: String) {
        context.dataStore.edit { it[PreferencesKeys.ACCENT_COLOR_HEX] = colorHex }
    }
}
