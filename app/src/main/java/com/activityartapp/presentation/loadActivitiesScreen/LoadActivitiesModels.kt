package com.activityartapp.presentation.loadActivitiesScreen

import com.activityartapp.architecture.ViewEvent
import com.activityartapp.architecture.ViewState

sealed interface LoadActivitiesViewEvent : ViewEvent {
    object ClickedContinue : LoadActivitiesViewEvent
    object ClickedRetry : LoadActivitiesViewEvent
    object ClickedReturn : LoadActivitiesViewEvent
}

sealed interface LoadActivitiesViewState : ViewState {
    val totalActivitiesLoaded: Int

    data class Loading(override val totalActivitiesLoaded: Int = 0) : LoadActivitiesViewState
    data class LoadErrorNoInternet(
        override val totalActivitiesLoaded: Int = 0,
        val retrying: Boolean = false
    ) : LoadActivitiesViewState
}