package com.company.athleteapiart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.company.athleteapiart.presentation.athletescreen.AthleteScreen
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
        .appendQueryParameter("scope", "activity:read_all")
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
                        startDestination = if (uri == null) "login_screen" else "athlete_screen"
                    ) {
                        composable("login_screen") {
                            LoginScreen {
                                startActivity(loginIntent)
                            }
                        }
                        composable("athlete_screen") {
                            AthleteScreen()
                        }
                    }
                }
            }
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
    }
}