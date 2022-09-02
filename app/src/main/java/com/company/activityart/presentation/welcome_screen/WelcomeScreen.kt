package com.company.activityart.presentation.welcome_screen

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.common.ScreenBackground
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewState.Loading
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewState.Standby
import com.company.activityart.presentation.welcome_screen.composables.WelcomeScreenStandby


/*

Welcome Screen
This is the screen users who are authenticated see first on opening the app.

https://developers.strava.com/guidelines/

 */

@Composable
fun WelcomeScreen(
    router: Router<MainDestination>,
    viewModel: WelcomeScreenViewModel = hiltViewModel()
) {
    ScreenBackground(spacedBy = spacing.medium) {
        viewModel.apply {
            attachRouter(router)
            viewState.collectAsState().value?.let {
                when (it) {
                    is Loading -> CircularProgressIndicator()
                    is Standby -> WelcomeScreenStandby(
                        state = it,
                        eventReceiver = viewModel
                    )
                    else -> {}
                }
            }
        }
    }
}
