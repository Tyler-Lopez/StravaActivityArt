package com.company.activityart.presentation.select_years_screen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.company.activityart.domain.models.Activity

sealed class SelectYearsViewEvent : ViewEvent {
    object ContinueClicked : SelectYearsViewEvent()
    object NavigateUpClicked : SelectYearsViewEvent()
}

sealed class SelectYearsViewState : ViewState {
    data class Loading(
        val totalActivitiesLoaded: Int
    ) : SelectYearsViewState()
    data class Standby(
        val isLoading: Boolean,
        val activitiesCountByYear: List<Pair<Int, Int>>
    ) : SelectYearsViewState()
}