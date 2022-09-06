package com.company.activityart.presentation

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import com.company.activityart.architecture.Router
import com.company.activityart.domain.models.Athlete
import com.company.activityart.presentation.about_screen.AboutScreen
import com.company.activityart.presentation.filter_year_screen.FilterYearScreen
import com.company.activityart.presentation.login_screen.LoginScreen
import com.company.activityart.presentation.welcome_screen.WelcomeScreen
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewModel
import com.company.activityart.util.NavArg.*
import com.company.activityart.util.Screen
import com.company.activityart.util.Screen.*
import com.company.activityart.util.StringConstants.SSH_ROUTER_KEY
import com.company.activityart.util.ext.swipingInOutComposable
import com.company.activityart.util.ext.swipingOutComposable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainNavHost(
    navController: NavHostController,
    startRoute: String,
    router: Router<MainDestination>,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        swipingOutComposable(route = Login.route) {
            LoginScreen(router)
        }
        swipingOutComposable(route = Welcome.route) {
            WelcomeScreen(router)
        }
        swipingInOutComposable(route = About.route) {
            AboutScreen(router)
        }
        swipingInOutComposable(
            route = FilterYear.route +
                    "?${AthleteId.route}&${AccessToken.route}",
            arguments = listOf(AthleteId.navArg, AccessToken.navArg)
        ) {
            FilterYearScreen(router)
        }
    }
}