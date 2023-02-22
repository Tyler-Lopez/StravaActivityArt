package com.activityartapp.presentation

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
import com.activityartapp.architecture.Router
import com.activityartapp.presentation.MainDestination.*
import com.activityartapp.presentation.MainViewEvent.LoadAuthentication
import com.activityartapp.presentation.MainViewState.Authenticated
import com.activityartapp.presentation.ui.theme.AthleteApiArtTheme
import com.activityartapp.util.NavArgSpecification.*
import com.activityartapp.util.Screen.*
import com.activityartapp.util.constants.TokenConstants.authUri
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity(), Router<MainDestination> {

    private lateinit var navController: NavHostController

    @Inject
    lateinit var gson: Gson

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // Push event to ViewModel to determine authentication
        val intentUri = intent.data.also { intent.data = null }
        super.onCreate(savedInstanceState)

        // Install Splash Screen before content is setContent
        val splashScreen = installSplashScreen()
        setContent {
            viewModel = hiltViewModel()

            viewModel.apply {
                /** Set splash screen to on while loading authentication **/
                splashScreen.setKeepOnScreenCondition {
                    viewState.value == null
                }
                /** Push event to [MainViewModel] to determine authentication **/
                LaunchedEffect(key1 = intentUri) {
                    println("Intent uri is $intentUri")
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
            is NavigateError -> onNavigateError(destination)
            is NavigateEditArt -> navigateMakeArt(destination)
            is NavigateSaveArt -> navigateSaveArt(destination)
            is NavigateUp -> navigateUp()
            is ShareFile -> shareFile(destination)
        }
    }

    private fun connectWithStrava() {
        finish()
        println("Connecting with Strava, authUri is $authUri")
        startActivity(Intent(ACTION_VIEW, authUri))
    }

    private fun navigateAbout() {
        navController.navigate(About.route)
    }

    private fun navigateLoadActivities(destination: NavigateLoadActivities) {
        destination.apply {
            navController.navigate(
                route = LoadActivities.route
            )
        }
    }

    private fun navigateLogin() {
        navController.navigate(route = Login.route) {
            popUpTo(route = Welcome.route) {
                inclusive = true
            }
        }
    }

    private fun onNavigateError(destination: NavigateError) {
        navController.navigate(
            route = Error.withArgs(
                args = arrayOf(
                    ErrorScreenArg to destination.errorScreenType.toString()
                )
            )
        ) {
            if (destination.clearNavigationHistory) {
                popUpTo(route = Welcome.route) {
                    inclusive = true
                }
                val a = ""
                val b: List<String> = a.mapIndexed { index, _ ->
                    a.substring(index, a.lastIndex)
                }
            }
        }
    }

    private fun navigateMakeArt(destination: NavigateEditArt) {
        navController.navigate(
            route = EditArt.withArgs(
                args = arrayOf(
                    AthleteIdArg to destination.athleteId.toString(),
                )
            )
        ) {
            destination.apply {
                popUpTo(route = LoadActivities.route) { inclusive = true }
            }
        }
    }

    private fun navigateSaveArt(destination: NavigateSaveArt) {
        destination.apply {
            navController.navigate(
                route = SaveArt.withArgs(
                    args = arrayOf(
                        ActivityTypesArg to gson.toJson(activityTypes.map { it.toString() }),
                        AthleteIdArg to athleteId.toString(),
                        BackgroundGradientAngleTypeArg to backgroundAngleType.toString(),
                        BackgroundTypeArg to backgroundType.toString(),
                        ColorActivitiesArgbArg to colorActivitiesArgb.toString(),
                        ColorBackgroundArgbListArg to gson.toJson(backgroundColorsArgb.map { it.toString() }),
                        ColorFontArgbArg to colorFontArgb.toString(),
                        FilterDateAfterMsArg to filterAfterMs.toString(),
                        FilterDateBeforeMsArg to filterBeforeMs.toString(),
                        FilterDistanceLessThanMetersArg to filterDistanceLessThanMeters.toString(),
                        FilterDistanceMoreThanMetersArg to filterDistanceMoreThanMeters.toString(),
                        SizeHeightPxArg to sizeHeightPx.toString(),
                        SizeWidthPxArg to sizeWidthPx.toString(),
                        SortTypeArg to sortType.toString(),
                        SortDirectionTypeArg to sortDirectionType.toString(),
                        StrokeWidthArg to strokeWidthType.toString(),
                        TextLeftArg to (textLeft ?: ""),
                        TextCenterArg to (textCenter ?: ""),
                        TextRightArg to (textRight ?: ""),
                        TextFontAssetPathArg to textFontAssetPath,
                        TextFontSizeArg to textFontSize.toString()
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
        intent.putExtra(EXTRA_TEXT, "I made this art from my activities with Activity Art!")
        intent.putExtra(EXTRA_SUBJECT, "Activity Art")
        intent.type = "image/png"
        val chooserIntent = createChooser(intent, "Share Via")
        chooserIntent.addFlags(FLAG_ACTIVITY_NEW_TASK) // Allows running this on Android 11 outside of Activity
        applicationContext.startActivity(chooserIntent)
    }
}