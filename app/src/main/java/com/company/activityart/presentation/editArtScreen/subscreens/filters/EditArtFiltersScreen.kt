package com.company.activityart.presentation.editArtScreen.subscreens.filters

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.PathNode
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.editArtScreen.EditArtFilterType
import com.company.activityart.presentation.editArtScreen.EditArtFilterType.*
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent
import com.company.activityart.presentation.editArtScreen.subscreens.filters.composables.FilterSectionActivityType
import com.company.activityart.presentation.editArtScreen.subscreens.filters.composables.FilterSectionDate
import com.company.activityart.presentation.editArtScreen.subscreens.filters.composables.FilterSectionDistances
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.util.classes.YearMonthDay

@Composable
fun EditArtFiltersScreen(
    dateMaxDateSelectedYearMonthDay: YearMonthDay?,
    dateMinDateSelectedYearMonthDay: YearMonthDay?,
    dateMaxDateTotalYearMonthDay: YearMonthDay?,
    dateMinDateTotalYearMonthDay: YearMonthDay?,
    distanceSelected: ClosedFloatingPointRange<Double>?,
    distanceTotal: ClosedFloatingPointRange<Double>?,
    typesWithSelectedFlag: List<Pair<String, Boolean>>,
    scrollState: ScrollState,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Column(
        modifier = Modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(spacing.medium)
    ) {
        EditArtFilterType.values().onEach {
            when (it) {
                DATE -> if (
                    dateMaxDateTotalYearMonthDay != null &&
                    dateMinDateTotalYearMonthDay != null
                ) FilterSectionDate(
                    dateMaxDateSelectedYearMonthDay = dateMaxDateSelectedYearMonthDay,
                    dateMinDateSelectedYearMonthDay = dateMinDateSelectedYearMonthDay,
                    dateMaxDateTotalYearMonthDay = dateMaxDateTotalYearMonthDay,
                    dateMinDateTotalYearMonthDay = dateMinDateTotalYearMonthDay,
                    eventReceiver = eventReceiver,
                    selectedActivities = 5
                )
                TYPE -> FilterSectionActivityType(
                    typesWithSelectedFlag = typesWithSelectedFlag,
                    eventReceiver = eventReceiver
                )
                DISTANCE -> if (distanceTotal != null) {
                    FilterSectionDistances(
                        distanceSelected = distanceSelected,
                        distanceTotal = distanceTotal,
                        eventReceiver = eventReceiver
                    )
                }
            }
        }
    }
}