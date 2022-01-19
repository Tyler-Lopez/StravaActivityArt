package com.company.athleteapiart

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.company.athleteapiart.presentation.activity_visualize_screen.ActivitiesScreen
import com.company.athleteapiart.presentation.login_screen.LoginScreen
import com.company.athleteapiart.presentation.time_select_screen.TimeSelectScreen
import com.company.athleteapiart.ui.theme.AthleteApiArtTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Create view model with URI if received
        val viewModel = MainViewModel(uri = intent.data)


        setContent {
            AthleteApiArtTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login_screen"
                ) {
                    /*

                    Login screen should handle:
                        * If user is using app for first time, must give access through Strava
                        * Process authorization code into token if necessary
                        * If user is returning and has valid access token redirect to time-select
                        * If necessary get access token


                     */
                    composable("login_screen") {
                        LoginScreen(
                            navController = navController,
                            onClick = { startActivity(it) }
                        )
                    }
                    composable("activity_screen") {
                        ActivitiesScreen(navController = navController)
                    }
                    composable("time_select_screen") {
                        TimeSelectScreen(navController = navController)
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
    }

}