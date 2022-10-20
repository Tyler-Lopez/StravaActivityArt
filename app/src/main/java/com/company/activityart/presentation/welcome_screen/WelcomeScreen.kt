package com.company.activityart.presentation.welcome_screen

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.company.activityart.presentation.common.ScreenBackgroundLegacy
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.presentation.welcome_screen.WelcomeViewState.Loading
import com.company.activityart.presentation.welcome_screen.WelcomeViewState.Standby
import com.company.activityart.presentation.welcome_screen.composables.WelcomeScreenStandby


/*

Welcome Screen
This is the screen users who are authenticated see first on opening the app.

https://developers.strava.com/guidelines/

 */

@Composable
fun WelcomeScreen(viewModel: WelcomeViewModel) {
    ScreenBackgroundLegacy(spacedBy = spacing.medium) {
        viewModel.viewState.collectAsState().value?.apply {
            when (this) {
                is Loading -> CircularProgressIndicator()
                is Standby -> WelcomeScreenStandby(
                    athleteImageUrl = athleteImageUrl,
                    athleteName = athleteName,
                    eventReceiver = viewModel
                )
                else -> {}
            }
        }
    }
}
