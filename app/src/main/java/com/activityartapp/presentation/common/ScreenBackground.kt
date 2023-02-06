package com.activityartapp.presentation.common

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Arrangement.Vertical
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Horizontal
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun ScreenBackground(
    modifier: Modifier = Modifier,
    horizontalAlignment: Horizontal = CenterHorizontally,
    padding: Dp = spacing.medium,
    scrollState: ScrollState? = rememberScrollState(),
    scrollingEnabled: Boolean = true,
    verticalArrangement: Vertical = spacedBy(
        space = spacing.medium,
        alignment = CenterVertically
    ),
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        content = content,
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
            .fillMaxSize()
            .run { if (scrollingEnabled) scrollState?.let { verticalScroll(it) } ?: this else this }
            .padding(padding),
        verticalArrangement = verticalArrangement
    )
}