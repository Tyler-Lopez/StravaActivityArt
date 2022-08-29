package com.company.activityart.presentation.login_screen

import android.net.Uri
import androidx.navigation.NavController
import com.company.activityart.MainViewEvent
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed class LoginScreenViewEvent : ViewEvent {
    data class ConnectWithStravaClicked(
        val mainEventReceiver: EventReceiver<MainViewEvent>
    ) : LoginScreenViewEvent()

    data class LoadAccessToken(val uri: Uri?) : LoginScreenViewEvent()
}

sealed class LoginScreenViewState : ViewState {
    object Authenticated : LoginScreenViewState()
    object Launch : LoginScreenViewState()
    object Loading : LoginScreenViewState()
    object Standby : LoginScreenViewState()
}