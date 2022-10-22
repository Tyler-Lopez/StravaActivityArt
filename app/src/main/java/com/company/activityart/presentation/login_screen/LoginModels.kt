package com.company.activityart.presentation.login_screen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed interface LoginViewEvent : ViewEvent {
    object ConnectWithStravaClicked : LoginViewEvent
}

data class LoginViewState(
    val textUsername: String = String(),
    val textPassword: String = String()
) : ViewState