package com.activityartapp.presentation.loadActivitiesScreen

import com.activityartapp.architecture.ViewEvent
import com.activityartapp.architecture.ViewState
import com.activityartapp.util.classes.ApiError

sealed interface LoadActivitiesViewEvent : ViewEvent {
    object ClickedContinue : LoadActivitiesViewEvent
    object ClickedReconnectWithStrava : LoadActivitiesViewEvent
    object ClickedRetry : LoadActivitiesViewEvent
    object ClickedReturn : LoadActivitiesViewEvent
}

sealed interface LoadActivitiesViewState : ViewState {
    val totalActivitiesLoaded: Int

    data class Loaded(override val totalActivitiesLoaded: Int) : LoadActivitiesViewState
    data class Loading(override val totalActivitiesLoaded: Int = 0) : LoadActivitiesViewState

    data class ErrorApi(
        val error: ApiError,
        val retrying: Boolean,
        override val totalActivitiesLoaded: Int = 0
    ) : LoadActivitiesViewState
    object ErrorNoActivities : LoadActivitiesViewState {
        override val totalActivitiesLoaded: Int = 0
    }
}