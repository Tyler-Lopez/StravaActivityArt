package com.activityartapp.presentation.editArtScreen.subscreens.filters.composables

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.activityartapp.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FilterCount(count: Int) {
    Text(
        text = count.takeIf { it > 0 }?.let {
            pluralStringResource(
                id = R.plurals.edit_art_filters_activities_filtered,
                count = count,
                count
            )
        } ?: stringResource(id = R.string.edit_art_filters_activities_filtered_zero),
        style = MaterialTheme.typography.caption,
        color = if (count > 0) {
            MaterialTheme.colors.primary
        } else {
            MaterialTheme.colors.error
        }
    )
}