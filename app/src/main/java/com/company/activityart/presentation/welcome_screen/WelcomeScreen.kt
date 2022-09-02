package com.company.activityart.presentation.welcome_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.common.LoadingComposable
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
                    is Loading -> LoadingComposable()
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
/*
when (it) {

    is Launch -> WelcomeScreenLaunchState(athleteId, accessToken, viewModel)
    is LoadError -> WelcomeScreenLoadError(viewModel, navController)
    is Loading -> LoadingComposable()
    is Standby -> WelcomeScreenStandbyState(it, viewModel, navController)
}

 */
