package com.company.activityart.presentation.filter_year_screen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed class FilterYearViewEvent : ViewEvent {
    object NavigateUpClicked : FilterYearViewEvent()
}

sealed class FilterYearViewState : ViewState {
    data class Standby(
        val selectedActivitiesCount: Int
    ) : FilterYearViewState()
}