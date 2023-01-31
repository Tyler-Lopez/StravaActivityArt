package com.activityartapp.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun ColumnSmallSpacing(content: @Composable ColumnScope.() -> Unit) {
    Column(
        content = content,
        verticalArrangement = Arrangement.spacedBy(spacing.small)
    )
}
