package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.company.activityart.presentation.common.type.Body
import com.company.activityart.presentation.common.type.TitleTwo
import com.company.activityart.presentation.ui.theme.spacing

@Composable
fun Section(
    header: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .padding(spacing.medium),
        verticalArrangement = Arrangement.spacedBy(spacing.medium)
    ) {
        TitleTwo(text = header)
        Body(text = description)
        content()
    }
}