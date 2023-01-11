package com.activityartapp.presentation.loginScreen

import com.activityartapp.architecture.ViewEvent
import com.activityartapp.architecture.ViewState

sealed interface LoginViewEvent : ViewEvent {
    object ConnectWithStravaClicked : LoginViewEvent
}

data class LoginViewState(
    val textUsername: String = String(),
    val textPassword: String = String()
) : ViewState