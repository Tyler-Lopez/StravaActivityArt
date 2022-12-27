package com.company.activityart.presentation

import android.content.Intent
import android.content.Intent.*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.LaunchedEffect
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
import com.company.activityart.util.NavArgSpecification.*
import com.company.activityart.util.Screen.*
import com.company.activityart.util.constants.TokenConstants.authUri
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity(), Router<MainDestination> {

    lateinit var navController: NavHostController

    @Inject
    lateinit var gson: Gson

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
                    /** Set splash screen to on while loading authentication **/
                    splashScreen.setKeepOnScreenCondition {
                        viewState.value is LoadingAuthentication
                    }
                    /** Push event to [MainViewModel] to determine authentication **/
                    LaunchedEffect(key1 = intentUri) {
                        onEvent(LoadAuthentication(intentUri))
                    }
                    /** Set global nav controller for [routeTo] **/
                    navController = rememberAnimatedNavController()


                    viewState.collectAsState().value?.let {
                        val startScreen = if (it is Authenticated) {
                            Welcome.route
                        } else {
                            Login.route
                        }
                        AthleteApiArtTheme {
                            MainNavHost(
                                navController = navController,
                                startRoute = startScreen,
                                router = this@MainActivity,
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
            is NavigateLoadActivities -> navigateLoadActivities(destination)
            is NavigateLogin -> navigateLogin()
            is NavigateEditArt -> navigateMakeArt(destination)
            is NavigateSaveArt -> navigateSaveArt(destination)
            is NavigateUp -> navigateUp()
            is ShareFile -> shareFile(destination)
        }
    }

    private fun connectWithStrava() {
        finish()
        startActivity(Intent(ACTION_VIEW, authUri))
    }

    private fun navigateAbout() {
        navController.navigate(About.route)
    }

    private fun navigateLoadActivities(destination: NavigateLoadActivities) {
        destination.apply {
            navController.navigate(
                route = LoadActivities.withArgs(
                    args = arrayOf(
                        AthleteId to athleteId,
                        AccessToken to accessToken
                    )
                )
            )
        }
    }

    private fun navigateLogin() {
        navController.navigate(route = Login.route) {
            popUpTo(
                route = Welcome.route
                // todo identify what this was doing
                //+
                //     "?${athleteIdNavSpec.route}&${accessTokenNavSpec.route}"
            ) {
                inclusive = true
            }
        }
    }

    private fun navigateMakeArt(destination: NavigateEditArt) {
        navController.navigate(EditArt.route) {
            destination.apply {
                if (fromLoad) {
                    popUpTo(
                        route = LoadActivities.buildRoute(
                            listOf(
                                AthleteId,
                                AccessToken
                            )
                        )
                    ) { inclusive = true }
                }
            }
        }
    }

    private fun navigateSaveArt(destination: NavigateSaveArt) {
        destination.apply {
            navController.navigate(
                route = SaveArt.withArgs(
                    args = arrayOf(
                        ActivityTypes to gson.toJson(activityTypes),
                        ColorActivitiesArgb to colorActivitiesArgb.toString(),
                        ColorBackgroundArgb to colorBackgroundArgb.toString(),
                        FilterDateAfterMs to filterAfterMs.toString(),
                        FilterDateBeforeMs to filterBeforeMs.toString(),
                        SizeHeightPx to sizeHeightPx.toString(),
                        SizeWidthPx to sizeWidthPx.toString(),
                        StrokeWidth to strokeWidthType.toString()
                    )
                )
            )
        }
    }

    private fun navigateUp() {
        navController.navigateUp()
    }

    private fun shareFile(shareFile: ShareFile) {
        val intent = Intent(ACTION_SEND)
        intent.putExtra(EXTRA_STREAM, shareFile.uri)
        intent.putExtra(EXTRA_TEXT, "Check out this art I made from my activities!")
        intent.putExtra(EXTRA_SUBJECT, "Activity Art")
        intent.type = "image/png"
        applicationContext.startActivity(createChooser(intent, "Share Via"))
    }
}