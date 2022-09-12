package com.company.activityart.presentation.make_art_screen.subscreens.filters

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewEventListener
import com.company.activityart.architecture.ViewState
import com.company.activityart.presentation.make_art_screen.EditArtViewEvent
import com.company.activityart.util.YearMonthDay

sealed class EditArtFiltersViewEvent : ViewEvent {
    data class DistanceRangeChanged(
        val oldUnixSecondsRange: ClosedFloatingPointRange<Float>,
        val newUnixSecondsRange: ClosedFloatingPointRange<Float>
    ) : EditArtFiltersViewEvent()
}

sealed class EditArtFiltersViewState : ViewState {
    object LoadingFilters : EditArtFiltersViewState()
    data class Standby(
        val dateEarliestSelected: YearMonthDay,
        val dateLatestSelected: YearMonthDay,
        val unixSecondsRangeSelected: ClosedFloatingPointRange<Float>,
        val unixSecondsRangeTotal: ClosedFloatingPointRange<Float>,
        val distanceMax: Double,
        val distanceMin: Double,
        val selectedActivitiesCount: Int
    ) : EditArtFiltersViewState()
}