package com.company.activityart.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.about_screen.AboutScreen
import com.company.activityart.presentation.about_screen.AboutViewModel
import com.company.activityart.presentation.load_activities_screen.LoadActivitiesScreen
import com.company.activityart.presentation.load_activities_screen.LoadActivitiesViewModel
import com.company.activityart.presentation.login_screen.LoginScreen
import com.company.activityart.presentation.edit_art_screen.EditArtViewDelegate
import com.company.activityart.presentation.edit_art_screen.EditArtViewModel
import com.company.activityart.presentation.login_screen.LoginViewModel
import com.company.activityart.presentation.welcome_screen.WelcomeScreen
import com.company.activityart.presentation.welcome_screen.WelcomeViewModel
import com.company.activityart.util.NavArg.*
import com.company.activityart.util.Screen.*
import com.company.activityart.util.ext.swipingInOutComposable
import com.company.activityart.util.ext.swipingOutComposable
import com.google.accompanist.navigation.animation.AnimatedNavHost

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
            LoginScreen(hiltViewModel<LoginViewModel>().apply {
                attachRouter(router)
            })
        }
        swipingOutComposable(route = Welcome.route) {
            WelcomeScreen(hiltViewModel<WelcomeViewModel>().apply {
                attachRouter(router)
            })
        }
        swipingInOutComposable(route = About.route) {
            AboutScreen(hiltViewModel<AboutViewModel>().apply {
                attachRouter(router)
            })
        }
        /** TODO, determine if activities have been Singleton cached and
         * if so skip this screen **/
        swipingInOutComposable(
            route = LoadActivities.route +
                    "?${AthleteId.route}&${AccessToken.route}",
            arguments = listOf(AthleteId.navArg, AccessToken.navArg)
        ) {
            LoadActivitiesScreen(hiltViewModel<LoadActivitiesViewModel>().apply {
                LaunchedEffect(router) { attachRouter(router) }
            })
        }
        swipingInOutComposable(route = EditArt.route) {
            EditArtViewDelegate(viewModel = hiltViewModel<EditArtViewModel>().apply {
                LaunchedEffect(router) { attachRouter(router) }
            })
        }
    }
}