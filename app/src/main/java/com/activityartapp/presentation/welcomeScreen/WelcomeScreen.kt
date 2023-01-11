package com.activityartapp.presentation.welcomeScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.activityartapp.presentation.AppLogo
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.welcomeScreen.composables.WelcomeNoInternet
import com.activityartapp.presentation.welcomeScreen.composables.WelcomeStandby


/*

Welcome Screen
This is the screen users who are authenticated see first on opening the app.

https://developers.strava.com/guidelines/

 */

@Composable
fun WelcomeScreen(viewModel: WelcomeViewModel) {
    ScreenBackground {
        AppLogo()
        viewModel.viewState.collectAsState().value?.apply {
            when (this) {
                is WelcomeViewState.Standby -> WelcomeStandby(
                    athleteName = athleteName,
                    athleteImageUrl = athleteImageUrl,
                    eventReceiver = viewModel
                )
                is WelcomeViewState.NoInternet -> WelcomeNoInternet(
                    retrying = retrying,
                    eventReceiver = viewModel
                )
            }
        }

    }
}
