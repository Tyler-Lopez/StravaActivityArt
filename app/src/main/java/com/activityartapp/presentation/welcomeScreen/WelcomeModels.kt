package com.activityartapp.presentation.welcomeScreen

import com.activityartapp.architecture.ViewEvent
import com.activityartapp.architecture.ViewState

sealed interface WelcomeViewEvent : ViewEvent {
    object ClickedAbout : WelcomeViewEvent
    object ClickedMakeArt : WelcomeViewEvent
    object ClickedLogout : WelcomeViewEvent
    object ClickedRetryConnection : WelcomeViewEvent
}

sealed interface WelcomeViewState : ViewState {
    data class Standby(
        val athleteName: String,
        val athleteImageUrl: String,
    ) : WelcomeViewState
    data class NoInternet(
        val retrying: Boolean
    ) : WelcomeViewState
}
