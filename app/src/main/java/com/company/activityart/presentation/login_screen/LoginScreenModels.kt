package com.company.activityart.presentation.login_screen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed class LoginScreenViewEvent : ViewEvent {
    object ConnectWithStravaClicked : LoginScreenViewEvent()
}

sealed class LoginScreenViewState : ViewState {
    object Standby : LoginScreenViewState()
}