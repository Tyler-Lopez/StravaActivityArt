package com.activityartapp.presentation.welcomeScreen

import com.activityartapp.architecture.ViewEvent
import com.activityartapp.architecture.ViewState
import com.activityartapp.util.classes.ApiError
import com.activityartapp.util.enums.ErrorType

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
    data class Error(
        val error: ApiError.UserFacingError,
        val retrying: Boolean
    ) : WelcomeViewState
}
