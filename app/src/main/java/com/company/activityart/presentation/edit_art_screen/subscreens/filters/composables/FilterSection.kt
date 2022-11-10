package com.company.activityart.presentation.edit_art_screen.subscreens.filters.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import com.company.activityart.R
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.Section
import com.company.activityart.presentation.ui.theme.Typography
import okhttp3.internal.format

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FilterSection(
    description: String,
    filteredActivityCount: Int,
    filterType: String,
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
            text = pluralStringResource(
                id = R.plurals.edit_art_filters_activities_filtered,
                count = filteredActivityCount,
                filterType, filteredActivityCount
            ),
            style = Typography.subtitle1,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.pumpkin)
        )
    }
}