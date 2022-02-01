package com.company.athleteapiart

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.navigation.compose.composable
import com.company.athleteapiart.presentation.activity_visualize_screen.ActivitiesScreen
import com.company.athleteapiart.presentation.filter_activities_screen.FilterActivitiesScreen
import com.company.athleteapiart.presentation.format_screen.FormatScreenOne
import com.company.athleteapiart.presentation.format_screen.FormatScreenTwo
import com.company.athleteapiart.presentation.login_screen.LoginScreen
import com.company.athleteapiart.presentation.time_select_screen.TimeSelectScreen
import com.company.athleteapiart.ui.theme.AthleteApiArtTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Create view model with URI if received

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)




        setContent {
            AthleteApiArtTheme {
                val viewModel = MainViewModel(uri = intent.data)
                var isLoading by remember { mutableStateOf(true) }

                if (isLoading)
                    LoginScreen(onClick = { intent ->
                        if (intent == null) {
                            isLoading = false
                        } else {
                            startActivity(intent)
                        }
                    })
                else {
                    val navController = rememberAnimatedNavController()
                    AnimatedNavHost(
                        navController,
                        startDestination = "${Screen.TimeSelect}",
                        enterTransition = { EnterTransition.None },
                        exitTransition = { ExitTransition.None },
                        popEnterTransition = { EnterTransition.None },
                        popExitTransition = { ExitTransition.None }
                    ) {
                        composable(
                            "${Screen.TimeSelect}"
                        ) {
                            TimeSelectScreen(navController)
                        }
                        composable(route = "${Screen.FilterActivities}") {
                            FilterActivitiesScreen(navController)
                        }
                        composable(route = "${Screen.FormatActivitiesOne}") {
                            FormatScreenOne(navController)
                        }
                        composable(route = "${Screen.FormatActivitiesTwo}") {
                            FormatScreenTwo(navController)
                        }
                        composable(route = "${Screen.VisualizeActivities}") {
                            ActivitiesScreen(navController)
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
    }
}