package com.company.activityart.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.aboutScreen.AboutScreen
import com.company.activityart.presentation.aboutScreen.AboutViewModel
import com.company.activityart.presentation.editArtScreen.EditArtViewDelegate
import com.company.activityart.presentation.editArtScreen.EditArtViewModel
import com.company.activityart.presentation.loadActivitiesScreen.LoadActivitiesScreen
import com.company.activityart.presentation.loadActivitiesScreen.LoadActivitiesViewModel
import com.company.activityart.presentation.loginScreen.LoginScreen
import com.company.activityart.presentation.loginScreen.LoginViewModel
import com.company.activityart.presentation.saveArtScreen.SaveArtViewDelegate
import com.company.activityart.presentation.saveArtScreen.SaveArtViewModel
import com.company.activityart.presentation.welcomeScreen.WelcomeScreen
import com.company.activityart.presentation.welcomeScreen.WelcomeViewModel
import com.company.activityart.util.NavArg
import com.company.activityart.util.NavArg.AccessToken
import com.company.activityart.util.NavArg.AthleteId
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
                    "?${NavArg.AthleteId.route}&${NavArg.AccessToken.route}",
            arguments = listOf(NavArg.AthleteId.navArg, NavArg.AccessToken.navArg)
        ) {
            LoadActivitiesScreen(hiltViewModel<LoadActivitiesViewModel>().apply {
                attachRouter(router)
            })
        }
        swipingInOutComposable(route = EditArt.route) {
            EditArtViewDelegate(viewModel = hiltViewModel<EditArtViewModel>().apply {
                attachRouter(router)
            })
        }
        swipingInOutComposable(
            route = SaveArt.route + "/{${NavArg.Bitmap.key}}",
            arguments = listOf(NavArg.Bitmap.navArg)
        ) {
            SaveArtViewDelegate(viewModel = hiltViewModel<SaveArtViewModel>().apply {
                attachRouter(router)
            })
        }
    }
}