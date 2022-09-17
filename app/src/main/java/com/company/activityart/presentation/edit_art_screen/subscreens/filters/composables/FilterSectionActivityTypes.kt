package com.company.activityart.presentation.edit_art_screen.subscreens.filters.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.FilterSection

@Composable
fun FilterSectionActivityTypes() {
    FilterSection(
        header = stringResource(R.string.edit_art_filters_activity_type_header),
        description = stringResource(R.string.edit_art_filters_activity_type_description),
    ) {

    }
}