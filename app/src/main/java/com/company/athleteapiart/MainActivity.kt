package com.company.athleteapiart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.company.athleteapiart.presentation.about_screen.AboutScreen
import com.company.athleteapiart.presentation.activity_visualize_screen.ActivitiesScreen
import com.company.athleteapiart.presentation.filter_distance_screen.FilterDistanceScreen
import com.company.athleteapiart.presentation.filter_gear_screen.FilterGearScreen
import com.company.athleteapiart.presentation.filter_month_screen.FilterMonthScreen
import com.company.athleteapiart.presentation.filter_type_screen.FilterTypeScreen
import com.company.athleteapiart.presentation.login_screen.LoginScreen
import com.company.athleteapiart.presentation.save_image_screen.SaveImageScreen
import com.company.athleteapiart.presentation.time_select_screen.TimeSelectScreen
import com.company.athleteapiart.presentation.ui.theme.AthleteApiArtTheme
import com.company.athleteapiart.presentation.welcome_screen.WelcomeScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val viewModel = MainViewModel()

        // Don't love the way of doing this
        // The reason for this is to only fetch from URI on activity creation and never after
        // So we use it once, then set it null
        var receivedFromUri: Uri? = intent.data

        super.onCreate(savedInstanceState)
        setContent {
            AthleteApiArtTheme {

                val navController = rememberAnimatedNavController()
                AnimatedNavHost(navController, startDestination = Screen.Login.route) {
                    // Authenticating the user with Strava and retrieving access token
                    composable(Screen.Login.route) {
                        LoginScreen(
                            // Use URI then set null
                            uri = receivedFromUri.also { receivedFromUri = null },
                            navController = navController,
                            onLoginIntent = {
                                // Send us to the screen to connect with Strava and
                                // prevent going back to this activity after
                                finish()
                                startActivity(it)
                            }
                        )
                    }
                    composable(
                        route = Screen.Welcome.route + "/{athleteId}/{accessToken}",
                        arguments = listOf(
                            navArgument("athleteId") {
                                type = NavType.LongType
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
                        route = Screen.TimeSelect.route + "/{athleteId}/{accessToken}",
                        arguments = listOf(
                            navArgument("athleteId") {
                                type = NavType.LongType
                                nullable = false
                            },
                            navArgument("accessToken") {
                                type = NavType.StringType
                                nullable = false
                            }
                        )
                    ) { entry ->
                        TimeSelectScreen(
                            athleteId = entry.arguments?.getLong("athleteId") ?: -1,
                            accessToken = entry.arguments?.getString("accessToken") ?: "null",
                            navController = navController
                        )
                    }
                    composable(
                        route = Screen.FilterMonth.route + "/{athleteId}/{accessToken}/{yearsRaw}",
                        arguments = listOf(
                            navArgument("athleteId") {
                                type = NavType.LongType
                                nullable = false
                            },
                            navArgument("accessToken") {
                                type = NavType.StringType
                                nullable = false
                            },
                            navArgument("yearsRaw") {
                                type = NavType.StringType
                                nullable = false
                            }
                        )
                    ) { entry ->
                        val years = viewModel
                            .parseYearsFromNav(entry.arguments?.getString("yearsRaw"))

                        FilterMonthScreen(
                            athleteId = entry.arguments?.getLong("athleteId") ?: -1,
                            accessToken = entry.arguments?.getString("accessToken") ?: "null",
                            years = years,
                            navController = navController
                        )
                    }
                    composable(
                        route = Screen.FilterType.route + "/{athleteId}/{accessToken}/{yearMonths}",
                        arguments = listOf(
                            navArgument("athleteId") {
                                type = NavType.LongType
                                nullable = false
                            },
                            navArgument("accessToken") {
                                type = NavType.StringType
                                nullable = false
                            },
                            navArgument("yearMonths") {
                                type = NavType.StringType
                                nullable = false
                            }
                        )
                    ) { entry ->
                        val yearMonths = viewModel
                            .parseYearMonthsFromNav(entry.arguments?.getString("yearMonths"))

                        FilterTypeScreen(
                            athleteId = entry.arguments?.getLong("athleteId") ?: -1,
                            accessToken = entry.arguments?.getString("accessToken") ?: "null",
                            yearMonths = yearMonths,
                            navController = navController
                        )
                    }
                    composable(
                        route = Screen.FilterGear.route + "/{athleteId}/{accessToken}/{yearMonths}?types={types}",
                        arguments = listOf(
                            navArgument("athleteId") {
                                type = NavType.LongType
                                nullable = false
                            },
                            navArgument("accessToken") {
                                type = NavType.StringType
                                nullable = false
                            },
                            navArgument("yearMonths") {
                                type = NavType.StringType
                                nullable = false
                            },
                            navArgument("types") {
                                type = NavType.StringType
                                nullable =
                                    true // NULLABLE, if null there is only 1 type - do not filter
                            }
                        )
                    ) { entry ->
                        val yearMonths = viewModel
                            .parseYearMonthsFromNav(entry.arguments?.getString("yearMonths"))
                        val activityTypesArg = entry.arguments?.getString("types")
                        val activityTypes =
                            if (activityTypesArg != null)
                                viewModel.parseTypesFromNav(activityTypesArg)
                            else null

                        FilterGearScreen(
                            athleteId = entry.arguments?.getLong("athleteId") ?: -1,
                            accessToken = entry.arguments?.getString("accessToken") ?: "null",
                            yearMonths = yearMonths,
                            navController = navController,
                            activityTypes = activityTypes
                        )
                    }
                    composable(
                        route = Screen.FilterDistance.route + "/{athleteId}/{accessToken}/{yearMonths}?types={types}&gears={gears}",
                        arguments = listOf(
                            navArgument("athleteId") {
                                type = NavType.LongType
                                nullable = false
                            },
                            navArgument("accessToken") {
                                type = NavType.StringType
                                nullable = false
                            },
                            navArgument("yearMonths") {
                                type = NavType.StringType
                                nullable = false
                            },
                            navArgument("types") {
                                type = NavType.StringType
                                nullable = true // NULLABLE, if null there is only 1 type - do not filter
                            },
                            navArgument("gears") {
                                type = NavType.StringType
                                nullable = true // NULLABLE, if null there is only 1 gear - do not filter
                            }
                        )
                    ) { entry ->
                        val yearMonths = viewModel
                            .parseYearMonthsFromNav(entry.arguments?.getString("yearMonths"))

                        val activityTypes = null // TODO
                        val gears = null // TODO

                        FilterDistanceScreen(
                            athleteId = entry.arguments?.getLong("athleteId") ?: -1,
                            yearMonths = yearMonths,
                            navController = navController,
                            activityTypes = activityTypes,
                            gears = gears
                        )
                    }
                    composable(Screen.VisualizeActivities.route) {
                        ActivitiesScreen(navController = navController)
                    }
                    composable(Screen.SaveImage.route) {
                        SaveImageScreen(navController = navController)
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