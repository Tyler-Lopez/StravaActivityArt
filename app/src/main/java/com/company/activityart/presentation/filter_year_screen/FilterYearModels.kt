package com.company.activityart.presentation.filter_year_screen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.company.activityart.domain.models.Activity

sealed class FilterYearViewEvent : ViewEvent {
    object ContinueClicked : FilterYearViewEvent()
    object NavigateUpClicked : FilterYearViewEvent()
}

sealed class FilterYearViewState : ViewState {
    object Loading : FilterYearViewState()
    data class Standby(
        val selectedActivitiesCount: Int,
        val selectedActivities: List<Activity>
    ) : FilterYearViewState()
}