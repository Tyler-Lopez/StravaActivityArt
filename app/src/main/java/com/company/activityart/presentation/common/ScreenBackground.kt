package com.company.activityart.presentation.edit_art_screen.subscreens.filters.composables

import androidx.compose.foundation.ScrollState
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
import androidx.compose.ui.unit.dp

@Composable
fun ScreenBackground(
    modifier: Modifier = Modifier,
    horizontalAlignment: Horizontal = CenterHorizontally,
    scrollState: ScrollState = rememberScrollState(),
    verticalArrangement: Vertical = spacedBy(
        space = 16.dp,
        alignment = CenterVertically
    ),
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        content = content,
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = verticalArrangement
    )
}