package com.company.activityart.presentation.editArtScreen.subscreens.filters

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent
import com.company.activityart.presentation.editArtScreen.subscreens.filters.composables.FilterSectionActivityType
import com.company.activityart.presentation.editArtScreen.subscreens.filters.composables.FilterSectionDate
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.util.classes.YearMonthDay

@Composable
fun EditArtFiltersScreen(
    dateMaxDateSelectedYearMonthDay: YearMonthDay,
    dateMinDateSelectedYearMonthDay: YearMonthDay,
    dateMaxDateTotalYearMonthDay: YearMonthDay,
    dateMinDateTotalYearMonthDay: YearMonthDay,
    typesWithSelectedFlag: List<Pair<String, Boolean>>,
    scrollState: ScrollState,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Column(
        modifier = Modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(spacing.medium)
    ) {
        FilterSectionDate(
            dateMaxDateSelectedYearMonthDay = dateMaxDateSelectedYearMonthDay,
            dateMinDateSelectedYearMonthDay = dateMinDateSelectedYearMonthDay,
            dateMaxDateTotalYearMonthDay = dateMaxDateTotalYearMonthDay,
            dateMinDateTotalYearMonthDay = dateMinDateTotalYearMonthDay,
            eventReceiver = eventReceiver,
            selectedActivities = 5
        )
        FilterSectionActivityType(
            typesWithSelectedFlag = typesWithSelectedFlag,
            eventReceiver = eventReceiver
        )
    }
}