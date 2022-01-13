package com.company.athleteapiart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.company.athleteapiart.presentation.activity_select_screen.ActivitySelectScreen
import com.company.athleteapiart.presentation.athletescreen.ActivityScreen
import com.company.athleteapiart.presentation.login_screen.LoginScreen
import com.company.athleteapiart.ui.theme.AthleteApiArtTheme
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
                            ActivityScreen()
                        }
                    }
                }
            }
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
    }

}