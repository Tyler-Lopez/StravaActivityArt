package com.company.athleteapiart

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.company.athleteapiart.presentation.activity_visualize_screen.ActivitiesScreen
import com.company.athleteapiart.presentation.login_screen.LoginScreen
import com.company.athleteapiart.presentation.time_select_screen.TimeSelectScreen
import com.company.athleteapiart.ui.theme.AthleteApiArtTheme
import com.company.athleteapiart.util.Oauth2
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    /*
    private val intentUri: Uri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
        .buildUpon()
        .appendQueryParameter("client_id", "75992")
        .appendQueryParameter("redirect_uri", "com.company.athleteapiart://myapp.com")
        .appendQueryParameter("response_type", "code")
        .appendQueryParameter("approval_prompt", "auto")
        .appendQueryParameter("scope", "read,read_all,activity:read,activity:read_all")
        .build()

    private val loginIntent = Intent(Intent.ACTION_VIEW, intentUri)

     */


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Handle URI response if received
        val uri = intent.data
        // If received, parse out authorizationCode (distinct from access token)
        if (uri != null) Oauth2.authorizationCode = uri
            .toString()
            .substring(43)
            .substringBefore('&')



        setContent {
            AthleteApiArtTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = if (uri == null) "login_screen" else "time_select_screen"
                ) {
                    /*

                    This screen should handle if user has previously logged in and should return
                    the URI request if necessary

                     */
                    composable("login_screen") {
                        LoginScreen({ startActivity(it) })
                    }
                    //   composable("activities_screen") {
                    //       ActivitySelectScreen() {
                    //           navController.navigate("activity_screen")
                    //       }
                    //   }
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