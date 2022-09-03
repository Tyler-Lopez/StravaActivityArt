package com.company.activityart.presentation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.End
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.*
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.about_screen.AboutScreen
import com.company.activityart.presentation.common.ScreenBackground
import com.company.activityart.presentation.login_screen.LoginScreen
import com.company.activityart.presentation.welcome_screen.WelcomeScreen
import com.company.activityart.util.Screen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Start
import androidx.compose.animation.slideOut
import com.company.activityart.util.ext.swipingComposable
import com.google.accompanist.navigation.animation.composable

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
        swipingComposable(route = Screen.Login.route) {
            LoginScreen(router)
        }
        swipingComposable(route = Screen.Welcome.route) {
            WelcomeScreen(router)
        }
        swipingComposable(route = Screen.About.route) {
            AboutScreen(router)
        }
    }

}