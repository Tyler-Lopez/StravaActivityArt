package com.company.activityart.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.*
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.login_screen.LoginScreen
import com.company.activityart.presentation.welcome_screen.WelcomeScreen
import com.company.activityart.util.Screen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavHost(
    navController: NavHostController,
    startScreen: Screen
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startScreen.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen()
        }
        composable(Screen.Welcome.route) {
            WelcomeScreen()
        }
    }
}