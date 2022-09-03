package com.company.activityart.presentation

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.MainViewEvent.LoadAuthentication
import com.company.activityart.presentation.MainViewState.Authenticated
import com.company.activityart.presentation.MainViewState.LoadingAuthentication
import com.company.activityart.presentation.ui.theme.AthleteApiArtTheme
import com.company.activityart.util.Screen.*
import com.company.activityart.util.TokenConstants.authUri
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity(), Router<MainDestination> {

    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        // Push event to ViewModel to determine authentication
        val intentUri = intent.data.also { intent.data = null }
        super.onCreate(savedInstanceState)
        // Install Splash Screen before content is setContent
        val splashScreen = installSplashScreen()
        setContent {
            AthleteApiArtTheme {
                val viewModel: MainViewModel = hiltViewModel()
                viewModel.apply {
                    splashScreen.setKeepOnScreenCondition {
                        viewState.value is LoadingAuthentication
                    }
                    // Push event to ViewModel to determine authentication
                    onEvent(LoadAuthentication(intentUri))
                    navController = rememberAnimatedNavController()

                    viewState.collectAsState().value?.let {
                        val startScreen = if (it is Authenticated) Welcome else Login
                        AthleteApiArtTheme {
                            MainNavHost(
                                navController = navController,
                                startScreen = startScreen,
                                router = this@MainActivity
                            )
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

    override fun routeTo(destination: MainDestination) {
        when (destination) {
            is ConnectWithStrava -> connectWithStrava()
            is NavigateAbout -> navigateAbout()
            is NavigateLogin -> navigateLogin()
            is NavigateMakeArt -> {}
            is NavigateUp -> navigateUp()
        }
    }

    private fun connectWithStrava() {
        finish()
        startActivity(Intent(ACTION_VIEW, authUri))
    }

    private fun navigateAbout() {
        navController.navigate(About.route)
    }

    private fun navigateLogin() {
        navController.navigate(route = Login.route) {
            popUpTo(route = Welcome.route + "/{athleteId}/{accessToken}") {
                inclusive = true
            }
        }
    }

    private fun navigateMakeArt() {

    }

    private fun navigateUp() {
        navController.navigateUp()
    }
}
/*
Column(
    modifier = Modifier
        .fillMaxSize()
        .background(Icicle),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {

    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }
    ) {
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
            route = Screen.FilterYear.route + "/{athleteId}/{accessToken}",
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
            FilterYearScreen(
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
            println("here activitytypes are $activityTypesArg in navigation")
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
            route = Screen.FilterDistance.route + "/{athleteId}/{yearMonths}?types={types}&gears={gears}",
            arguments = listOf(
                navArgument("athleteId") {
                    type = NavType.LongType
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
                },
                navArgument("gears") {
                    type = NavType.StringType
                    nullable =
                        true // NULLABLE, if null there is only 1 gear - do not filter
                }
            )
        ) { entry ->

            val args = entry.arguments!!

            val yearMonths = args.getString("yearMonths").let {
                viewModel.parseYearMonthsFromNav(it)
            }
            val gears = args.getString("gears").let {
                viewModel.parseGearsFromNav(it)
            }
            val activityTypes = entry.arguments?.getString("types").let {
                viewModel.parseTypesFromNav(it)
            }

            FilterDistanceScreen(
                athleteId = entry.arguments?.getLong("athleteId") ?: -1,
                yearMonths = yearMonths,
                navController = navController,
                activityTypes = activityTypes,
                gears = gears
            )
        }
        composable(
            route = Screen.VisualizeActivities.route
                    + "/{athleteId}/{yearMonths}?types={types}&gears={gears}&distances={distances}",
            arguments = listOf(
                navArgument("athleteId") {
                    type = NavType.LongType
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
                },
                navArgument("gears") {
                    type = NavType.StringType
                    nullable =
                        true // NULLABLE, if null there is only 1 gear - do not filter
                },
                navArgument("distances") {
                    type = NavType.StringType
                    nullable =
                        true // NULLABLE, if null there is only 1 gear - do not filter
                }
            )
        ) { entry ->
            val args = entry.arguments!!

            val yearMonths = args.getString("yearMonths").let {
                viewModel.parseYearMonthsFromNav(it)
            }
            val gears = args.getString("gears").let {
                viewModel.parseGearsFromNav(it)
            }
            val activityTypes = entry.arguments?.getString("types").let {
                viewModel.parseTypesFromNav(it)
            }
            val distances = entry.arguments?.getString("distances").let {
                viewModel.parseDistancesFromNav(it)
            }

            VisualizeScreen(
                navController = navController,
                athleteId = args.getLong("athleteId"),
                yearMonths = yearMonths,
                activityTypes = activityTypes,
                gears = gears,
                distances = distances
            )
        }
    }
}
}

 */
