package com.activityartapp.presentation.editArtScreen.subscreens.filters

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.editArtScreen.DateSelection
import com.activityartapp.presentation.editArtScreen.EditArtFilterType
import com.activityartapp.presentation.editArtScreen.EditArtFilterType.*
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.activityart.presentation.editArtScreen.subscreens.filters.composables.FilterSectionActivityType
import com.activityartapp.presentation.editArtScreen.subscreens.filters.composables.FilterSectionDate
import com.activityartapp.presentation.editArtScreen.subscreens.filters.composables.FilterSectionDistances
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun EditArtFiltersScreen(
    activitiesCountDate: Int,
    activitiesCountDistance: Int,
    activitiesCountType: Int,
    filterDateSelections: List<DateSelection>?,
    filterDateSelectionIndex: Int,
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
                DATE -> if (filterDateSelections != null)
                    FilterSectionDate(
                        count = activitiesCountDate,
                        dateSelections = filterDateSelections,
                        dateSelectionSelectedIndex = filterDateSelectionIndex,
                        eventReceiver = eventReceiver
                    )
                TYPE -> FilterSectionActivityType(
                    count = activitiesCountType,
                    typesWithSelectedFlag = typesWithSelectedFlag,
                    eventReceiver = eventReceiver
                )
                DISTANCE -> if (distanceTotal != null) {
                    FilterSectionDistances(
                        count = activitiesCountDistance,
                        distanceSelected = distanceSelected,
                        distanceTotal = distanceTotal,
                        eventReceiver = eventReceiver
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}