package com.activityartapp.presentation.editArtScreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
    actionButton: @Composable (() -> Unit)? = {},
    excludePadding: Boolean = false,
    includeDivider: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    ColumnMediumSpacing(
        modifier = Modifier
            .background(MaterialTheme.colors.surface)
            .padding(
                if (!excludePadding) {
                    spacing.medium
                } else {
                    0.dp
                }
            ),
        horizontalAlignment = Alignment.Start
    ) {
        ColumnMediumSpacing(
            modifier = Modifier.padding(
                if (excludePadding) {
                    spacing.medium
                } else {
                    0.dp
                }
            )
        ) {
            ColumnSmallSpacing(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = header,
                        style = MaterialTheme.typography.h5
                    )
                    actionButton?.invoke()
                }
                Divider()
            }
            description?.let {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.body1
                )
            }
        }
        ColumnSmallSpacing(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }
    }
}