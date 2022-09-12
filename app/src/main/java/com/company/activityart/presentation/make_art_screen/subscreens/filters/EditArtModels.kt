package com.company.activityart.presentation.make_art_screen.subscreens.filters

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed class EditArtFiltersViewEvent : ViewEvent {

}

sealed class EditArtFiltersViewState : ViewState {
    object LoadingFilters : EditArtFiltersViewState()
    data class Standby(
        val yearMonthEarliest: Pair<Int, Int>,
        val yearMonthLatest: Pair<Int, Int>,
        val distanceMax: Double,
        val distanceMin: Double,
        val selectedActivitiesCount: Int
    ) : EditArtFiltersViewState()
}