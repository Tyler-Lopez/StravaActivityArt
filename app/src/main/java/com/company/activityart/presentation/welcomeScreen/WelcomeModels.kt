package com.company.activityart.presentation.welcomeScreen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed interface WelcomeViewEvent : ViewEvent {
    object ClickedAbout : WelcomeViewEvent
    object ClickedMakeArt : WelcomeViewEvent
    object ClickedLogout : WelcomeViewEvent
}

data class WelcomeViewState(
    val athleteName: String,
    val athleteImageUrl: String,
) : ViewState