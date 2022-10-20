package com.company.activityart.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.company.activityart.presentation.ui.theme.White

@Composable
fun ScreenBackgroundLegacy(
    spacedBy: Dp = 0.dp,
    verticalAlignment: Alignment.Vertical = CenterVertically,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = spacedBy,
            alignment = verticalAlignment
        )
    ) {
        content()
    }
}