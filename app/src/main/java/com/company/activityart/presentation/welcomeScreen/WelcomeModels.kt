package com.company.activityart.presentation.welcomeScreen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

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
