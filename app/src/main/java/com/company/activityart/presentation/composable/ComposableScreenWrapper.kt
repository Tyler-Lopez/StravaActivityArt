package com.company.activityart.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.company.activityart.presentation.ui.spacing

@Composable
fun ComposableScreenWrapper(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isThin = this.maxWidth < 414.dp
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(if (isThin) 0.dp else MaterialTheme.spacing.md),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}