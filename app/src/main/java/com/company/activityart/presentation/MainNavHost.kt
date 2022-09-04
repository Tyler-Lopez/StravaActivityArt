package com.company.activityart.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.*
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.about_screen.AboutScreen
import com.company.activityart.presentation.filter_year_screen.FilterYearScreen
import com.company.activityart.presentation.login_screen.LoginScreen
import com.company.activityart.presentation.welcome_screen.WelcomeScreen
import com.company.activityart.util.Screen
import com.company.activityart.util.ext.swipingOutComposable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.company.activityart.util.ext.swipingInOutComposable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavHost(
    navController: NavHostController,
    startScreen: Screen,
    router: Router<MainDestination>
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startScreen.route
    ) {
        swipingOutComposable(route = Screen.Login.route) {
            LoginScreen(router)
        }
        swipingOutComposable(route = Screen.Welcome.route) {
            WelcomeScreen(router)
        }
        swipingInOutComposable(route = Screen.About.route) {
            AboutScreen(router)
        }
        swipingInOutComposable(route = Screen.FilterYear.route) {
            FilterYearScreen(router)
        }
    }

}