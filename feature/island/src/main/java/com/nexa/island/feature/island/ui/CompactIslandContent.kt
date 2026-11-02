package com.nexa.island.feature.island.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexa.island.core.design.theme.AccentCyan
import com.nexa.island.core.design.theme.AccentEmerald
import com.nexa.island.core.design.theme.AccentPurple
import com.nexa.island.core.design.theme.AccentRose
import com.nexa.island.core.design.theme.TextPrimary
import com.nexa.island.core.design.theme.TextSecondary
import com.nexa.island.domain.model.IslandEvent

@Composable
fun CompactIslandContent(
    event: IslandEvent?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (event) {
            is IslandEvent.Music -> {
                // Left: Tiny Music Icon / Disc
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(AccentPurple.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Music",
                            tint = AccentPurple,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = event.trackTitle,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(90.dp)
                    )
                }

                // Right: Mini Audio Visualizer Wave
                CompactVisualizer(isPlaying = event.isPlaying)
            }

            is IslandEvent.Notification -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(AccentCyan.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notification",
                            tint = AccentCyan,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = event.appName,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = event.title.ifBlank { "New message" },
                    color = TextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(80.dp)
                )
            }

            is IslandEvent.BatteryAlert -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BatteryChargingFull,
                        contentDescription = "Battery",
                        tint = if (event.isLowBattery) AccentRose else AccentEmerald,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (event.isCharging) "Charging" else "Battery",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = "${event.percentage}%",
                    color = if (event.isLowBattery) AccentRose else AccentEmerald,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            is IslandEvent.IncomingCall -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(AccentEmerald),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Text(
                        text = event.callerName,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Incoming...",
                    color = AccentEmerald,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            is IslandEvent.GamingHud -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsEsports,
                        contentDescription = "Game",
                        tint = AccentCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = event.gameTitle,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = "${event.fpsEstimate} FPS",
                    color = AccentCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            else -> {
                // Default Idle Pill with subtle Nexa Indicator
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(AccentPurple)
                )
                Text(
                    text = "Nexa",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(AccentCyan)
                )
            }
        }
    }
}

@Composable
fun CompactVisualizer(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val anim1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(350), RepeatMode.Reverse),
        label = "bar1"
    )
    val anim2 by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(420), RepeatMode.Reverse),
        label = "bar2"
    )
    val anim3 by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(280), RepeatMode.Reverse),
        label = "bar3"
    )

    Row(
        modifier = Modifier.height(14.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .fillMaxHeight(if (isPlaying) anim1 else 0.3f)
                .clip(RoundedCornerShape(2.dp))
                .background(AccentPurple)
        )
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .fillMaxHeight(if (isPlaying) anim2 else 0.5f)
                .clip(RoundedCornerShape(2.dp))
                .background(AccentCyan)
        )
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .fillMaxHeight(if (isPlaying) anim3 else 0.2f)
                .clip(RoundedCornerShape(2.dp))
                .background(AccentPurple)
        )
    }
}
