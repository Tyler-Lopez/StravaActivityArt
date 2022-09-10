package com.company.activityart.presentation.select_years_screen.composables

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.architecture.ViewEventListener
import com.company.activityart.presentation.common.button.ButtonSize.*
import com.company.activityart.presentation.common.button.HighEmphasisButton
import com.company.activityart.presentation.select_years_screen.SelectYearsViewEvent
import com.company.activityart.presentation.select_years_screen.SelectYearsViewEvent.*

@Composable
fun FilterYearScreenStandby(
    activitiesCountByYear: List<Pair<Int, Int>>,
    eventReceiver: ViewEventListener<SelectYearsViewEvent>,
) {
    LazyColumn {
        items(activitiesCountByYear.size) {
            Text(text = "${activitiesCountByYear[it].second}")
        }
    }
    HighEmphasisButton(
        text = stringResource(R.string.button_continue),
        modifier = Modifier.defaultMinSize(
            minWidth = dimensionResource(id = R.dimen.button_min_width)
        ),
        size = LARGE
    ) {
        eventReceiver.onEventDebounced(ContinueClicked)
    }
}