package com.company.activityart.presentation

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.about_screen.AboutScreen
import com.company.activityart.presentation.filter_year_screen.FilterYearScreen
import com.company.activityart.presentation.login_screen.LoginScreen
import com.company.activityart.presentation.welcome_screen.WelcomeScreen
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewModel
import com.company.activityart.util.Screen
import com.company.activityart.util.ext.swipingOutComposable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.company.activityart.util.ext.swipingInOutComposable
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
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
            val factory = EntryPointAccessors.fromActivity(
                LocalContext.current as Activity,
                MainActivity.ViewModelFactoryProvider::class.java
            ).welcomeScreenViewModelFactory()
            WelcomeScreen(
                router = router,
                viewModel = viewModel(
                    factory = WelcomeScreenViewModel.newInstance(
                        factory,
                        "HELPNEEEE"
                    )
                )
            )
          //  WelcomeScreen(router)
        }
        swipingInOutComposable(route = Screen.About.route) {
            AboutScreen(router)
        }
        swipingInOutComposable(route = Screen.FilterYear.route) {
            FilterYearScreen(router)
        }
    }

}