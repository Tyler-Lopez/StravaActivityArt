package com.activityartapp.presentation.welcomeScreen

import com.activityartapp.architecture.ViewEvent
import com.activityartapp.architecture.ViewState
import com.activityartapp.util.classes.ApiError
import com.activityartapp.util.enums.ErrorType

sealed interface WelcomeViewEvent : ViewEvent {
    object ClickedAbout : WelcomeViewEvent
    object ClickedMakeArt : WelcomeViewEvent
    object ClickedLogout : WelcomeViewEvent
}

sealed interface WelcomeViewState : ViewState {
    object Loading : WelcomeViewState
    data class Standby(val versionIsLatest: Boolean) : WelcomeViewState
    object ErrorUnsupportedVersion : WelcomeViewState
}