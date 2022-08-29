package com.company.activityart.presentation.login_screen

import android.media.metrics.Event
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.company.activityart.MainViewEvent
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.login_screen.LoginScreenViewEvent.*

@Composable
fun LoginScreenLaunchState(
    uri: Uri?,
    eventReceiver: EventReceiver<LoginScreenViewEvent>
) {
    SideEffect { eventReceiver.onEvent(LoadAccessToken(uri)) }
}

@Composable
fun LoginScreenStandbyState(
    mainEventReceiver: EventReceiver<MainViewEvent>
) {

}
