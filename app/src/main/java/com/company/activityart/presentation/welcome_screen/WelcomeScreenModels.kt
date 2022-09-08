package com.company.activityart.presentation.welcome_screen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed class WelcomeScreenViewEvent : ViewEvent {
    object ClickedAbout : WelcomeScreenViewEvent()
    object ClickedMakeArt : WelcomeScreenViewEvent()
    object ClickedLogout : WelcomeScreenViewEvent()
}

sealed class WelcomeScreenViewState : ViewState {
    data class LoadError(val message: String) : WelcomeScreenViewState()
    object Loading : WelcomeScreenViewState()
    data class Standby(
        val athleteName: String,
        val athleteImageUrl: String,
    ) : WelcomeScreenViewState()
}