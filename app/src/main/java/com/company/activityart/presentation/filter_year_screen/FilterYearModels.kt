package com.company.activityart.presentation.filter_year_screen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed class FilterYearViewEvent : ViewEvent {
    object ContinueClicked : FilterYearViewEvent()
    object NavigateUpClicked : FilterYearViewEvent()
}

sealed class FilterYearViewState : ViewState {
    object Loading : FilterYearViewState()
    data class Standby(
        val selectedActivitiesCount: Int
    ) : FilterYearViewState()
}