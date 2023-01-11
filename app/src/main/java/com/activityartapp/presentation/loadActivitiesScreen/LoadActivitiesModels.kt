package com.activityartapp.presentation.loadActivitiesScreen

import com.activityartapp.architecture.ViewEvent
import com.activityartapp.architecture.ViewState

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