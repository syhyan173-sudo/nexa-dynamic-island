package com.nexa.island.feature.island.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nexa.island.core.datastore.IslandSettings
import com.nexa.island.core.design.component.GlassSurface
import com.nexa.island.core.design.theme.IslandAnimationSpecs
import com.nexa.island.domain.model.ActiveIslandSession
import com.nexa.island.domain.model.IslandExpansionState

@Composable
fun DynamicIslandOverlayView(
    session: ActiveIslandSession,
    settings: IslandSettings,
    onToggleExpansion: () -> Unit,
    onDismissEvent: (String) -> Unit,
    onSendReply: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (session.expansionState == IslandExpansionState.HIDDEN || !settings.isIslandEnabled) {
        return
    }

    val interactionSource = remember { MutableInteractionSource() }

    val cornerRadius = when (session.expansionState) {
        IslandExpansionState.COMPACT -> settings.cornerRadiusDp.dp
        IslandExpansionState.EXPANDED -> 32.dp
        IslandExpansionState.HIDDEN -> 0.dp
    }

    val widthModifier = when (session.expansionState) {
        IslandExpansionState.COMPACT -> Modifier.width(settings.islandWidthDp.dp)
        IslandExpansionState.EXPANDED -> Modifier.fillMaxWidth(0.94f)
        IslandExpansionState.HIDDEN -> Modifier.width(0.dp)
    }

    val heightModifier = when (session.expansionState) {
        IslandExpansionState.COMPACT -> Modifier.heightIn(min = settings.islandHeightDp.dp, max = settings.islandHeightDp.dp)
        IslandExpansionState.EXPANDED -> Modifier.heightIn(min = 140.dp)
        IslandExpansionState.HIDDEN -> Modifier.heightIn(max = 0.dp)
    }

    GlassSurface(
        modifier = modifier
            .then(widthModifier)
            .then(heightModifier)
            .animateContentSize(animationSpec = IslandAnimationSpecs.islandSpring())
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onToggleExpansion
            ),
        shape = RoundedCornerShape(cornerRadius),
        elevation = if (session.expansionState == IslandExpansionState.EXPANDED) 16.dp else 8.dp
    ) {
        AnimatedContent(
            targetState = session.expansionState,
            transitionSpec = {
                fadeIn(animationSpec = IslandAnimationSpecs.islandSpring()) togetherWith
                        fadeOut(animationSpec = IslandAnimationSpecs.islandSpring())
            },
            label = "islandContentTransition",
            contentAlignment = Alignment.Center
        ) { targetState ->
            when (targetState) {
                IslandExpansionState.COMPACT -> {
                    CompactIslandContent(event = session.currentEvent)
                }
                IslandExpansionState.EXPANDED -> {
                    ExpandedIslandContent(
                        session = session,
                        onDismiss = {
                            session.currentEvent?.let { onDismissEvent(it.id) }
                        },
                        onSendReply = onSendReply
                    )
                }
                IslandExpansionState.HIDDEN -> {
                    Box(modifier = Modifier)
                }
            }
        }
    }
}
