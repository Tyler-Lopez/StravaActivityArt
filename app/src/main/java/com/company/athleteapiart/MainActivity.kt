package com.company.athleteapiart

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.composable
import com.company.athleteapiart.presentation.activity_visualize_screen.ActivitiesScreen
import com.company.athleteapiart.presentation.filter_activities_screen.FilterActivitiesScreen
import com.company.athleteapiart.presentation.format_screen.FormatScreenOne
import com.company.athleteapiart.presentation.format_screen.FormatScreenThree
import com.company.athleteapiart.presentation.format_screen.FormatScreenTwo
import com.company.athleteapiart.presentation.format_screen.FormatScreenFour
import com.company.athleteapiart.presentation.login_screen.LoginScreen
import com.company.athleteapiart.presentation.save_image_screen.SaveImageScreen
import com.company.athleteapiart.presentation.time_select_screen.TimeSelectScreen
import com.company.athleteapiart.ui.theme.AthleteApiArtTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Create view model with URI if received

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)




        setContent {
            AthleteApiArtTheme {
                val viewModel = MainViewModel(uri = intent.data)
                var isLoading by remember { mutableStateOf(true) }
                val navController = rememberAnimatedNavController()

                if (isLoading)
                    LoginScreen(onClick = { intent ->
                        if (intent == null) {
                            isLoading = false
                        } else {
                            startActivity(intent)
                        }
                    })
                else {
                    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                        val isThin = this.maxWidth < 414.dp

                        AnimatedNavHost(navController, startDestination = Screen.TimeSelect.route) {
                            composable(
                                route = Screen.TimeSelect.route,
                                enterTransition = { EnterTransition.None },
                                exitTransition = { ExitTransition.None },
                                popEnterTransition = { EnterTransition.None },
                                popExitTransition = { ExitTransition.None }
                            ) {
                                TimeSelectScreen(navController = navController)
                            }
                            composable(
                                route = Screen.FilterActivities.route,
                                enterTransition = { EnterTransition.None },
                                exitTransition = { ExitTransition.None },
                                popEnterTransition = { EnterTransition.None },
                                popExitTransition = { ExitTransition.None }
                            ) {
                                FilterActivitiesScreen(navController = navController)
                            }
                            composable(
                                route = Screen.FormatActivitiesOne.route,
                                enterTransition = { EnterTransition.None },
                                exitTransition = { ExitTransition.None },
                                popEnterTransition = { EnterTransition.None },
                                popExitTransition = { ExitTransition.None }
                            ) {
                                FormatScreenOne(navController = navController)
                            }
                            composable(
                                route = Screen.FormatActivitiesTwo.route,
                                enterTransition = { EnterTransition.None },
                                exitTransition = { ExitTransition.None },
                                popEnterTransition = { EnterTransition.None },
                                popExitTransition = { ExitTransition.None }
                            ) {
                                FormatScreenTwo(navController = navController)
                            }
                            composable(
                                route = Screen.FormatActivitiesThree.route,
                                enterTransition = { EnterTransition.None },
                                exitTransition = { ExitTransition.None },
                                popEnterTransition = { EnterTransition.None },
                                popExitTransition = { ExitTransition.None }
                            ) {
                                FormatScreenThree(navController = navController)
                            }
                            composable(
                                route = Screen.FormatActivitiesFour.route,
                                enterTransition = { EnterTransition.None },
                                exitTransition = { ExitTransition.None },
                                popEnterTransition = { EnterTransition.None },
                                popExitTransition = { ExitTransition.None }
                            ) {
                                FormatScreenFour(navController = navController)
                            }
                            composable(
                                route = Screen.VisualizeActivities.route,
                                enterTransition = { EnterTransition.None },
                                exitTransition = { ExitTransition.None },
                                popEnterTransition = { EnterTransition.None },
                                popExitTransition = { ExitTransition.None }
                            ) {
                                ActivitiesScreen(navController = navController)
                            }
                            composable(
                                route = Screen.SaveImage.route,
                                enterTransition = { EnterTransition.None },
                                exitTransition = { ExitTransition.None },
                                popEnterTransition = { EnterTransition.None },
                                popExitTransition = { ExitTransition.None }
                            ) {
                                SaveImageScreen(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}