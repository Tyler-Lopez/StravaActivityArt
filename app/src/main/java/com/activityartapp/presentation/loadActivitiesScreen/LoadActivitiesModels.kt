package com.activityartapp.presentation.loadActivitiesScreen

import com.activityartapp.architecture.ViewEvent
import com.activityartapp.architecture.ViewState

sealed interface LoadActivitiesViewEvent : ViewEvent {
    object ContinueClicked : LoadActivitiesViewEvent
    object TryAgainClicked : LoadActivitiesViewEvent
    object NavigateUpClicked : LoadActivitiesViewEvent
}

sealed interface LoadActivitiesViewState : ViewState {
    data class Loading(val totalActivitiesLoaded: Int = 0) : LoadActivitiesViewState
    data class LoadErrorNoInternet(val retrying: Boolean = false) : LoadActivitiesViewState
}