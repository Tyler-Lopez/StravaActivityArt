package com.activityartapp.presentation.loadActivitiesScreen.composables

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.common.type.SubheadHeavy

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoadActivitiesLoading(activitiesLoaded: Int) {
    CircularProgressIndicator()
    SubheadHeavy(
        text = activitiesLoaded.takeIf { it > 0 }?.let {
            pluralStringResource(
                id = R.plurals.loading_activities_count,
                count = activitiesLoaded,
                activitiesLoaded
            )
        } ?: stringResource(id = R.string.loading_activities_zero_count)
    )
}