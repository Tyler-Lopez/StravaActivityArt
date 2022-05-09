package com.company.athleteapiart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.company.athleteapiart.presentation.about_screen.AboutScreen
import com.company.athleteapiart.presentation.activity_visualize_screen.ActivitiesScreen
import com.company.athleteapiart.presentation.error_noactivities_screen.ErrorNoActivitiesScreen
import com.company.athleteapiart.presentation.filter_activities_screen.FilterActivitiesScreen
import com.company.athleteapiart.presentation.format_screen.FormatScreenOne
import com.company.athleteapiart.presentation.format_screen.FormatScreenThree
import com.company.athleteapiart.presentation.format_screen.FormatScreenTwo
import com.company.athleteapiart.presentation.format_screen.FormatScreenFour
import com.company.athleteapiart.presentation.login_screen.LoginScreen
import com.company.athleteapiart.presentation.save_image_screen.SaveImageScreen
import com.company.athleteapiart.presentation.time_select_screen.TimeSelectScreen
import com.company.athleteapiart.presentation.ui.theme.AthleteApiArtTheme
import com.company.athleteapiart.presentation.welcome_screen.WelcomeScreen
import com.company.athleteapiart.util.noAnimComposable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // Don't love the way of doing this
        // The reason for this is to only fetch from URI on activity creation and never after
        // So we use it once, then set it null
        var receivedFromUri: Uri? = intent.data

        super.onCreate(savedInstanceState)
        setContent {
            AthleteApiArtTheme {

                val navController = rememberAnimatedNavController()

                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    AnimatedNavHost(navController, startDestination = Screen.Login.route) {
                        // Authenticating the user with Strava and retrieving access token
                        composable(Screen.Login.route) {
                            LoginScreen(
                                // Use URI then set null
                                uri = receivedFromUri.also { receivedFromUri = null},
                                navController = navController,
                                onLoginIntent = {
                                    // Send us to the screen to connect with Strava and
                                    // prevent going back to this activity after
                                    finish()
                                    startActivity(it)
                                }
                            )
                        }
                        // Rec
                        composable(
                            route = Screen.Welcome.route + "/{athleteId}/{accessToken}",
                            arguments = listOf(
                                navArgument("athleteId") {
                                    type = NavType.IntType
                                    nullable = false
                                },
                                navArgument("accessToken") {
                                    type = NavType.StringType
                                    nullable = false
                                }
                            )
                        ) { entry ->
                            WelcomeScreen(
                                athleteId = entry.arguments?.getLong("athleteId") ?: -1,
                                accessToken = entry.arguments?.getString("accessToken") ?: "null",
                                navController = navController
                            )
                        }
                        // Simple screen showing information about application
                        composable(route = Screen.About.route) { AboutScreen() }
                        composable(
                            route = Screen.TimeSelect.route + "/{accessToken}",
                            arguments = listOf(
                                navArgument("accessToken") {
                                    type = NavType.StringType
                                    nullable = false
                                }
                            )
                        ) {
                            TimeSelectScreen(navController = navController)
                        }
                        composable(Screen.FilterActivities.route) {
                            FilterActivitiesScreen(navController = navController)
                        }
                        composable(Screen.FormatActivitiesOne.route) {
                            FormatScreenOne(navController = navController)
                        }
                        composable(Screen.FormatActivitiesTwo.route) {
                            FormatScreenTwo(navController = navController)
                        }
                        composable(Screen.FormatActivitiesThree.route) {
                            FormatScreenThree(navController = navController)
                        }
                        composable(Screen.FormatActivitiesFour.route) {
                            FormatScreenFour(navController = navController)
                        }
                        composable(Screen.VisualizeActivities.route) {
                            ActivitiesScreen(navController = navController)
                        }
                        composable(Screen.SaveImage.route) {
                            SaveImageScreen(navController = navController)
                        }
                        composable(Screen.ErrorNoActivities.route) {
                            ErrorNoActivitiesScreen(navController = navController)
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