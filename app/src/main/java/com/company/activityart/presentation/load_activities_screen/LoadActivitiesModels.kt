package com.company.activityart.presentation.load_activities_screen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.company.activityart.domain.models.Activity

sealed class LoadActivitiesViewEvent : ViewEvent {
    object ContinueClicked : LoadActivitiesViewEvent()
    object NavigateUpClicked : LoadActivitiesViewEvent()
}

sealed class LoadActivitiesViewState : ViewState {
    object Loading : LoadActivitiesViewState()
    data class Standby(
        val loadErrorOccurred: Boolean,
        val activitiesByYear: List<Pair<Int, List<Activity>>>
    ) : LoadActivitiesViewState()
}