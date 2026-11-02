package com.nexa.island.feature.dashboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexa.island.core.design.component.GlassCard
import com.nexa.island.core.design.theme.AccentCyan
import com.nexa.island.core.design.theme.AccentEmerald
import com.nexa.island.core.design.theme.AccentPurple
import com.nexa.island.core.design.theme.AccentRose
import com.nexa.island.core.design.theme.CharcoalBackground
import com.nexa.island.core.design.theme.TextMuted
import com.nexa.island.core.design.theme.TextPrimary
import com.nexa.island.core.design.theme.TextSecondary
import com.nexa.island.feature.dashboard.viewmodel.DashboardViewModel
import com.nexa.island.feature.island.ui.DynamicIslandOverlayView

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onRequestOverlayPermission: () -> Unit,
    onRequestNotificationPermission: () -> Unit,
    onOpenMiuiSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CharcoalBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Nexa Island",
                            color = TextPrimary,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "MIUI 13 & Android 12 Native Engine",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(AccentPurple.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "v1.0.0",
                            color = AccentPurple,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Live Island Sandbox Preview
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "LIVE ISLAND PREVIEW",
                            color = AccentCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Dynamic Island Sandbox Anchor
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFF0F172A)),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            // Punch hole camera simulator at top
                            Box(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black)
                            )

                            // Overlay view
                            Box(modifier = Modifier.padding(top = 12.dp)) {
                                DynamicIslandOverlayView(
                                    session = uiState.session,
                                    settings = uiState.settings,
                                    onToggleExpansion = { viewModel.toggleExpansion() },
                                    onDismissEvent = { viewModel.dismissEvent(it) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Tap the island above to toggle expand / compact states",
                            color = TextMuted,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Simulator event triggers
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.testMusicEvent() },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple.copy(alpha = 0.25f)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.MusicNote, contentDescription = null, tint = AccentPurple, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Music", color = TextPrimary, fontSize = 11.sp)
                            }

                            Button(
                                onClick = { viewModel.testNotificationEvent() },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentCyan.copy(alpha = 0.25f)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Notifications, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Notif", color = TextPrimary, fontSize = 11.sp)
                            }

                            Button(
                                onClick = { viewModel.testBatteryAlert() },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentEmerald.copy(alpha = 0.25f)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.BatteryChargingFull, contentDescription = null, tint = AccentEmerald, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Battery", color = TextPrimary, fontSize = 11.sp)
                            }

                            Button(
                                onClick = { viewModel.testIncomingCall() },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentRose.copy(alpha = 0.25f)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, tint = AccentRose, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call", color = TextPrimary, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // Permissions Checklist
            item {
                Text(
                    text = "Permission & Compatibility",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        PermissionRow(
                            title = "Display over other apps",
                            description = "Required to render the Dynamic Island overlay window",
                            isGranted = uiState.permissions.hasOverlayPermission,
                            onGrant = onRequestOverlayPermission
                        )
                        PermissionRow(
                            title = "Notification Listener",
                            description = "Reads incoming notifications for WhatsApp, Telegram, etc.",
                            isGranted = uiState.permissions.hasNotificationListenerPermission,
                            onGrant = onRequestNotificationPermission
                        )
                        PermissionRow(
                            title = "MIUI 13 Autostart & Battery",
                            description = "Prevents MIUI from killing the background overlay service",
                            isGranted = true,
                            onGrant = onOpenMiuiSettings
                        )
                    }
                }
            }

            // Feature Controls
            item {
                Text(
                    text = "Core Features",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SettingToggleRow(
                            title = "Enable Nexa Island",
                            subtitle = "Master switch for overlay engine",
                            checked = uiState.settings.isIslandEnabled,
                            onCheckedChange = { viewModel.toggleIslandEnabled(it) }
                        )
                        SettingToggleRow(
                            title = "Music Integration",
                            subtitle = "MediaSession controller & visualizer",
                            checked = uiState.settings.isMusicEnabled,
                            onCheckedChange = { viewModel.toggleMusicEnabled(it) }
                        )
                        SettingToggleRow(
                            title = "Smart Notifications",
                            subtitle = "Popups for chat & system events",
                            checked = uiState.settings.isNotificationEnabled,
                            onCheckedChange = { viewModel.toggleNotificationEnabled(it) }
                        )
                        SettingToggleRow(
                            title = "Gaming Mode HUD",
                            subtitle = "Compact auto-hide overlay when gaming",
                            checked = uiState.settings.isGamingModeEnabled,
                            onCheckedChange = { viewModel.toggleGamingModeEnabled(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionRow(
    title: String,
    description: String,
    isGranted: Boolean,
    onGrant: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isGranted) Icons.Default.CheckCircle else Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (isGranted) AccentEmerald else AccentRose,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
            Text(text = description, color = TextSecondary, fontSize = 12.sp)
        }

        if (!isGranted) {
            Button(
                onClick = onGrant,
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Grant", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = TextSecondary, fontSize = 12.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = AccentPurple,
                uncheckedTrackColor = Color(0x33FFFFFF)
            )
        )
    }
}
