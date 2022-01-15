package com.company.athleteapiart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.height
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.company.athleteapiart.presentation.activity_select_screen.ActivitySelectScreen
import com.company.athleteapiart.presentation.activity_visualize_screen.ActivitiesScreen
import com.company.athleteapiart.presentation.login_screen.LoginScreen
import com.company.athleteapiart.ui.theme.AthleteApiArtTheme
import com.company.athleteapiart.ui.theme.StravaOrange
import com.company.athleteapiart.util.Oauth2
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val intentUri: Uri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
        .buildUpon()
        .appendQueryParameter("client_id", "75992")
        .appendQueryParameter("redirect_uri", "com.company.athleteapiart://myapp.com")
        .appendQueryParameter("response_type", "code")
        .appendQueryParameter("approval_prompt", "auto")
        .appendQueryParameter("scope", "read,read_all,activity:read,activity:read_all")
        .build()

    private val loginIntent = Intent(Intent.ACTION_VIEW, intentUri)


    override fun onCreate(savedInstanceState: Bundle?) {

        val uri = intent.data
        if (uri != null)
            Oauth2.authorizationCode = uri.toString().substring(43).substringBefore('&')


        super.onCreate(savedInstanceState)
        setContent {
            AthleteApiArtTheme {
                Scaffold(
                    topBar = {
                        if (uri != null) {
                            TopAppBar(
                                backgroundColor = StravaOrange,
                                modifier = Modifier.height(50.dp)
                            ) {

                            }
                        }

                    }, content = {


                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = if (uri == null) "login_screen" else "activities_screen"
                        ) {
                            composable("login_screen") {
                                LoginScreen {
                                    startActivity(loginIntent)
                                }
                            }
                            composable("activities_screen") {
                                ActivitySelectScreen() {
                                    navController.navigate("activity_screen")
                                }
                            }
                            composable("activity_screen") {
                                ActivitiesScreen()
                            }
                        }
                    })
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
    }

}