package com.company.athleteapiart.presentation.welcome_screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.welcome_screen.WelcomeScreenState.*
import com.company.athleteapiart.presentation.login_screen.LoginScreenViewModel


/*

Welcome Screen

This is the screen users who are authenticated see first on opening the app.

https://developers.strava.com/guidelines/

 */

@Composable
fun WelcomeScreen(
    athleteId: Int,
    accessToken: String,
    navController: NavHostController,
    viewModel: WelcomeScreenViewModel = hiltViewModel()
) {
    val screenState by remember { viewModel.screenState }

    val context = LocalContext.current

    when (screenState) {
        LAUNCH -> SideEffect {
            viewModel.getAthlete(
                context = context,
                accessToken = accessToken
            )
        }
        LOADING -> Text("Loading")
        STANDBY -> {
            Column {
                Text("welcome screen $accessToken id is $athleteId")
                Text("${viewModel.athlete.value?.profileMedium}")
                Button(
                    onClick = {
                        viewModel.logout(context = context)
                    }
                ) {
                    Text("Logout")
                }
            }
        }
        LOGOUT ->
            LaunchedEffect(Unit) {
                println("HERE ${navController.currentBackStackEntry?.destination?.route}")
                navController.navigate(route = Screen.Login.route) {
                    popUpTo(route = Screen.Welcome.route + "/{athleteId}/{accessToken}") {
                        inclusive = true
                    }
                }
            }

    }

}