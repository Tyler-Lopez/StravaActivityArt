package com.company.activityart.presentation.load_activities_screen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed interface LoadActivitiesViewEvent : ViewEvent {
    object ContinueClicked : LoadActivitiesViewEvent
    object TryAgainClicked : LoadActivitiesViewEvent
    object NavigateUpClicked : LoadActivitiesViewEvent
}

sealed interface LoadActivitiesViewState : ViewState {
    val totalActivitiesLoaded: Int

    data class Loading(
        override val totalActivitiesLoaded: Int = 0
    ) : LoadActivitiesViewState

    data class LoadError(
        override val totalActivitiesLoaded: Int = 0
    ) : LoadActivitiesViewState
}