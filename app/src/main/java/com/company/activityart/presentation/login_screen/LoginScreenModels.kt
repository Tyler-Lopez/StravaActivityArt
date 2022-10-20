package com.company.activityart.presentation.login_screen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed interface LoginScreenViewEvent : ViewEvent {
    object ConnectWithStravaClicked : LoginScreenViewEvent
}

data class LoginScreenViewState(
    val textUsername: String = String(),
    val textPassword: String = String()
) : ViewState