package com.company.activityart.presentation.filter_year_screen.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.ViewEventListener
import com.company.activityart.domain.models.Activity
import com.company.activityart.presentation.common.button.ButtonSize.*
import com.company.activityart.presentation.common.button.HighEmphasisButton
import com.company.activityart.presentation.filter_year_screen.FilterYearViewEvent
import com.company.activityart.presentation.filter_year_screen.FilterYearViewEvent.*
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewEvent

@Composable
fun FilterYearScreenStandby(
    activities: List<Activity>,
    eventReceiver: ViewEventListener<FilterYearViewEvent>
) {
    LazyColumn {
        items(activities.size) {
            Text(text = activities[it].name)
        }
    }
    HighEmphasisButton(
        text = stringResource(R.string.button_continue),
        size = LARGE
    ) {
        eventReceiver.onEventDebounced(ContinueClicked)
    }
}