package com.company.activityart.presentation.welcome_screen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed interface WelcomeViewEvent : ViewEvent {
    object ClickedAbout : WelcomeViewEvent
    object ClickedMakeArt : WelcomeViewEvent
    object ClickedLogout : WelcomeViewEvent
}

sealed interface WelcomeViewState : ViewState {
    data class LoadError(val message: String) : WelcomeViewState
    object Loading : WelcomeViewState
    data class Standby(
        val athleteName: String,
        val athleteImageUrl: String,
    ) : WelcomeViewState
}