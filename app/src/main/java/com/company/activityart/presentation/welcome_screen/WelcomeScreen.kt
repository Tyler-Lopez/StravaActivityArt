package com.company.activityart.presentation.welcome_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.common.LoadingComposable
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewState.*


/*

Welcome Screen

This is the screen users who are authenticated see first on opening the app.

https://developers.strava.com/guidelines/

 */

@Composable
fun WelcomeScreen(
    onNavigateAbout: (NavigateAbout) -> Unit,
    onNavigateLogin: (NavigateLogin) -> Unit,
    onNavigateMakeArt: (NavigateMakeArt) -> Unit,
    viewModel: WelcomeScreenViewModel = hiltViewModel()
) {
    remember { viewModel.viewState }.value.let {
        when (it) {
            is Launch -> WelcomeScreenLaunchState(athleteId, accessToken, viewModel)
            is LoadError -> WelcomeScreenLoadError(viewModel, navController)
            is Loading -> LoadingComposable()
            is Standby -> WelcomeScreenStandbyState(it, viewModel, navController)
        }
    }
}