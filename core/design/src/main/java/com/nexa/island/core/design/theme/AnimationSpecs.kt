package com.nexa.island.core.design.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring

object IslandAnimationSpecs {
    // Configured for Redmi Note 9 60fps responsive fluid dynamics
    const val SPRING_DAMPING_RATIO = 0.82f
    const val SPRING_STIFFNESS = 350f

    fun <T> islandSpring() = spring<T>(
        dampingRatio = SPRING_DAMPING_RATIO,
        stiffness = SPRING_STIFFNESS
    )

    fun <T> bouncySpring() = spring<T>(
        dampingRatio = 0.75f,
        stiffness = Spring.StiffnessMediumLow
    )

    fun <T> snappySpring() = spring<T>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )
}
