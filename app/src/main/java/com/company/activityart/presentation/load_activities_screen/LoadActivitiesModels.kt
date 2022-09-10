package com.company.activityart.presentation.load_activities_screen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed class LoadActivitiesViewEvent : ViewEvent {
    object ContinueClicked : LoadActivitiesViewEvent()
    object TryAgainClicked : LoadActivitiesViewEvent()
    object NavigateUpClicked : LoadActivitiesViewEvent()
}

sealed class LoadActivitiesViewState : ViewState {
    data class Loading(
        val totalActivitiesLoaded: Int = 0
    ) : LoadActivitiesViewState()
    data class LoadError(
        val totalActivitiesLoaded: Int = 0
    ) : LoadActivitiesViewState()
}