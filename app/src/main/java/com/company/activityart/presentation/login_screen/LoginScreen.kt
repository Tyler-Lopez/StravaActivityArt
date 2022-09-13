package com.company.activityart.presentation.login_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.common.ScreenBackground
import com.company.activityart.presentation.login_screen.LoginScreenViewState.Standby
import com.company.activityart.presentation.login_screen.composables.LoginScreenStandby


/**
 * When the athlete is determined as unauthenticated,
 * this screen is the start destination.
 *
 * Presents a button which will prompt the athlete to
 * "Connect with Strava".
 */
@Composable
fun LoginScreen(
    router: Router<MainDestination>,
    viewModel: LoginScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(router) { viewModel.attachRouter(router) }
    ScreenBackground {
        viewModel.viewState.collectAsState().value?.apply {
            when (this) {
                is Standby -> LoginScreenStandby(viewModel)
            }
        }
    }
}