package com.activityartapp.presentation.editArtScreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.activityartapp.presentation.common.layout.ColumnMediumSpacing
import com.activityartapp.presentation.common.layout.ColumnSmallSpacing
import com.activityartapp.presentation.ui.theme.spacing

interface Section {
    val headerStrRes: Int
    val descriptionStrRes: Int?
}

@Composable
fun Section(
    header: String,
    description: String? = null,
    includeDivider: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    ColumnMediumSpacing(
        modifier = Modifier
            .background(MaterialTheme.colors.surface)
            .padding(spacing.medium),
        horizontalAlignment = Alignment.Start
    ) {
        ColumnSmallSpacing(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = header,
                style = MaterialTheme.typography.h5
            )
            Divider()
        }
        description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.body1
            )
        }
        ColumnSmallSpacing(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }
    }
}