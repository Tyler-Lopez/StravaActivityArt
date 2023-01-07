package com.company.activityart.presentation.loadActivitiesScreen.composables

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.pluralStringResource
import com.company.activityart.R
import com.company.activityart.presentation.common.type.SubheadHeavy

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoadActivitiesLoading(activitiesLoaded: Int) {
    CircularProgressIndicator()
    SubheadHeavy(
        text = pluralStringResource(
            id = R.plurals.loading_activities_count,
            count = activitiesLoaded,
            formatArgs = arrayOf(activitiesLoaded)
        ).uppercase()
    )
}