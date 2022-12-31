package com.company.activityart.presentation.editArtScreen.subscreens.filters.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import com.company.activityart.R
import com.company.activityart.presentation.editArtScreen.subscreens.filters.Section
import com.company.activityart.presentation.ui.theme.Typography

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FilterSection(
    count: Int,
    description: String,
    header: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Section(
        header = header,
        description = description,
        modifier = modifier
    ) {
        content()
        Text(
            text = count.takeIf { it > 0 }?.let {
                pluralStringResource(
                    id = R.plurals.edit_art_filters_activities_filtered,
                    count = count,
                    count
                )
            } ?: stringResource(id = R.string.edit_art_filters_activities_filtered_zero),
            style = Typography.subtitle1,
            fontWeight = FontWeight.Normal,
            color = colorResource(R.color.pumpkin)
        )
    }
}