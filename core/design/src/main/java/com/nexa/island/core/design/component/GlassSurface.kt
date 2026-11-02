package com.nexa.island.core.design.component

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nexa.island.core.design.theme.GlassBorder
import com.nexa.island.core.design.theme.GlassHighlight
import com.nexa.island.core.design.theme.TranslucentBlack

/**
 * Premium Dark Frosted Glassmorphism container.
 * Features GPU-optimized fallback for Redmi Note 9 (Android 12/MIUI 13)
 * combining subtle inner highlights, specular linear gradients, and soft elevation.
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(28.dp),
    backgroundColor: Color = TranslucentBlack,
    borderColor: Color = GlassBorder,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 8.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.5f),
                spotColor = Color.Black.copy(alpha = 0.6f)
            )
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor.copy(alpha = 0.88f),
                        backgroundColor.copy(alpha = 0.72f)
                    )
                )
            )
            .border(
                border = BorderStroke(borderWidth, borderColor),
                shape = shape
            )
    ) {
        // Subtle specular highlight on top rim
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        0.0f to GlassHighlight,
                        0.15f to Color.Transparent,
                        1.0f to Color.Transparent
                    )
                )
        )
        content()
    }
}
