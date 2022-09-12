package com.company.activityart.presentation.make_art_screen.subscreens.filters

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.company.activityart.presentation.common.type.Body
import com.company.activityart.presentation.common.type.TitleOne
import com.company.activityart.presentation.ui.theme.spacing

@Composable
fun FilterSection(
    header: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.padding(spacing.medium),
        verticalArrangement = Arrangement.spacedBy(spacing.medium)
    ) {
        TitleOne(text = header)
        Body(text = description)
        content()
    }
}