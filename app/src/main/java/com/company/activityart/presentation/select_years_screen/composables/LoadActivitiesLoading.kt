package com.company.activityart.presentation.select_years_screen.composables

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.pluralStringResource
import com.company.activityart.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoadActivitiesLoading(activitiesLoaded: Int) {
    CircularProgressIndicator()
    Text(
        text = pluralStringResource(
            id = R.plurals.loading_activities_count,
            count = activitiesLoaded,
            formatArgs = arrayOf(activitiesLoaded)
        )
    )
}