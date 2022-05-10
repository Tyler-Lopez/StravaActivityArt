package com.company.athleteapiart.presentation.welcome_screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.welcome_screen.WelcomeScreenState.*
import com.company.athleteapiart.presentation.login_screen.LoginScreenViewModel
import com.company.athleteapiart.presentation.ui.theme.StravaOrange
import com.google.android.gms.maps.model.Circle


/*

Welcome Screen

This is the screen users who are authenticated see first on opening the app.

https://developers.strava.com/guidelines/

 */

@Composable
fun WelcomeScreen(
    athleteId: Long,
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
                athleteId = athleteId,
                accessToken = accessToken
            )
        }
        LOADING -> Text("Loading")
        STANDBY -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(viewModel.athleteImageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                        .border(width = 5.dp, color = StravaOrange, shape = CircleShape)
                )
                Text("Welcome ${viewModel.athleteName}")
                // Navigate user to screen where they may select which years of activities to visualize
                Button(onClick = {
                    navController.navigate(
                        Screen.TimeSelect.withArgs(
                            athleteId.toString(),
                            accessToken
                        )
                    )
                }) {
                    Text("Make Activity Art")
                }
                // Navigates user to a simple screen showing information about app & author
                Button(onClick = { navController.navigate(route = Screen.About.route) }) {
                    Text("About")
                }
                // De-authenticates the user and clears OAuth2 database entry
                Button(onClick = { viewModel.logout(context = context) }) {
                    Text("Logout")
                }
                Text("also this athlete has cached this ${viewModel.athleteCacheThing}")
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