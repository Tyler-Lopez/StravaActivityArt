package com.company.activityart.presentation.login_screen

import android.net.Uri
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.MainViewEvent

sealed class LoginScreenViewEvent : ViewEvent {
    object ConnectWithStravaClicked : LoginScreenViewEvent()
}

sealed class LoginScreenViewState : ViewState {
    object Standby : LoginScreenViewState()
}