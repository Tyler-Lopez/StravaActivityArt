package com.company.activityart.presentation.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// https://www.youtube.com/watch?v=S61mXlMQkLY

data class Spacing(
    val xSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 32.dp,
    val extraLarge: Dp = 64.dp
)

/**
 * Provides Spacing for a Composable.
 */
val LocalSpacing = compositionLocalOf { Spacing() }

val spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current