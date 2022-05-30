package com.company.activityart.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ContainerColumn(
    maxWidth: Dp,
    content: @Composable ColumnScope.() -> Unit
) {
        Column(
            modifier = Modifier.widthIn(maxWidth * 0.75f, 360.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
}