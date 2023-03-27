package com.activityartapp.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.activityartapp.architecture.Router
import com.activityartapp.presentation.editArtScreen.EditArtViewDelegate
import com.activityartapp.presentation.editArtScreen.EditArtViewModel
import com.activityartapp.presentation.errorScreen.ErrorScreen
import com.activityartapp.presentation.errorScreen.ErrorViewModel
import com.activityartapp.presentation.loadActivitiesScreen.LoadActivitiesScreen
import com.activityartapp.presentation.loadActivitiesScreen.LoadActivitiesViewModel
import com.activityartapp.presentation.loginScreen.LoginScreen
import com.activityartapp.presentation.loginScreen.LoginViewModel
import com.activityartapp.presentation.saveArtScreen.SaveArtViewDelegate
import com.activityartapp.presentation.saveArtScreen.SaveArtViewModel
import com.activityartapp.presentation.welcomeScreen.WelcomeScreen
import com.activityartapp.presentation.welcomeScreen.WelcomeViewModel
import com.activityartapp.util.NavArgSpecification.*
import com.activityartapp.util.Screen.*
import com.activityartapp.util.ext.swipingInOutComposable
import com.activityartapp.util.ext.swipingOutComposable
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
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
        swipingOutComposable(Login) {
            LoginScreen(hiltViewModel<LoginViewModel>().apply {
                attachRouter(router)
            })
        }
        swipingOutComposable(Welcome) {
            WelcomeScreen(hiltViewModel<WelcomeViewModel>().apply {
                attachRouter(router)
            })
        }
        /** TODO, determine if activities have been Singleton cached and
         * if so skip this screen **/
        swipingInOutComposable(
            screen = LoadActivities
        ) {
            LoadActivitiesScreen(hiltViewModel<LoadActivitiesViewModel>().apply {
                attachRouter(router)
            })
        }
        swipingInOutComposable(
            screen = EditArt,
            navArgSpecifications = listOf(
                AthleteIdArg
            )
        ) {
            EditArtViewDelegate(viewModel = hiltViewModel<EditArtViewModel>().apply {
                attachRouter(router)
            })
        }
        swipingInOutComposable(
            screen = SaveArt,
            navArgSpecifications = listOf(
                ActivityColorRulesArg,
                ActivityTypesArg,
                AthleteIdArg,
                BackgroundGradientAngleTypeArg,
                BackgroundTypeArg,
                ColorActivitiesArgbArg,
                ColorBackgroundArgbListArg,
                ColorFontArgbArg,
                FilterDateAfterMsArg,
                FilterDateBeforeMsArg,
                FilterDistanceLessThanMetersArg,
                FilterDistanceMoreThanMetersArg,
                SizeHeightPxArg,
                SizeWidthPxArg,
                SortDirectionTypeArg,
                SortTypeArg,
                StrokeWidthArg,
                TextLeftArg,
                TextCenterArg,
                TextRightArg,
                TextFontAssetPathArg,
                TextFontSizeArg
            )
        ) {
            SaveArtViewDelegate(viewModel = hiltViewModel<SaveArtViewModel>().apply {
                attachRouter(router)
            })
        }
        swipingInOutComposable(
            screen = Error,
            navArgSpecifications = listOf(
                ErrorScreenArg
            )
        ) {
            ErrorScreen(viewModel = hiltViewModel<ErrorViewModel>().apply {
                attachRouter(router)
            })
        }
    }
}