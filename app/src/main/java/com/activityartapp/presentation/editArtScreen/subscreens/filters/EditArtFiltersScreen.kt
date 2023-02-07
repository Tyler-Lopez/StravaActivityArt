package com.activityartapp.presentation.editArtScreen.subscreens.filters

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.activityartapp.activityart.presentation.editArtScreen.subscreens.filters.composables.FilterSectionActivityType
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.editArtScreen.DateSelection
import com.activityartapp.presentation.editArtScreen.EditArtFilterType
import com.activityartapp.presentation.editArtScreen.EditArtFilterType.*
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.subscreens.filters.composables.FilterSectionDate
import com.activityartapp.presentation.editArtScreen.subscreens.filters.composables.FilterSectionDistances

@Composable
fun ColumnScope.EditArtFiltersScreen(
    activitiesCountDate: Int,
    activitiesCountDistance: Int,
    activitiesCountType: Int,
    filterDateSelections: List<DateSelection>?,
    filterDateSelectionIndex: Int,
    distanceSelected: ClosedFloatingPointRange<Double>?,
    distanceTotal: ClosedFloatingPointRange<Double>?,
    typesWithSelectedFlag: List<Pair<String, Boolean>>?,
    eventReceiver: EventReceiver<EditArtViewEvent>
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
            TYPE -> if (typesWithSelectedFlag != null)
                FilterSectionActivityType(
                    count = activitiesCountType,
                    typesWithSelectedFlag = typesWithSelectedFlag,
                    eventReceiver = eventReceiver
                )
            DISTANCE -> if (distanceTotal != null && distanceTotal.start != distanceTotal.endInclusive) {
                FilterSectionDistances(
                    count = activitiesCountDistance,
                    distanceSelected = distanceSelected,
                    distanceTotal = distanceTotal,
                    eventReceiver = eventReceiver
                )
            }
        }
    }
}