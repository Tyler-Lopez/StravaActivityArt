package com.activityartapp.presentation.editArtScreen.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.activityartapp.presentation.common.layout.ColumnSmallSpacing


@Composable
fun ColumnScope.Section(
    header: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Text(
        text = header,
        style = MaterialTheme.typography.h5
    )
    Divider()
    description?.let { Text(
        text = it,
        style = MaterialTheme.typography.body1
    ) }
    ColumnSmallSpacing(horizontalAlignment = Alignment.Start) {
        content()
    }
}