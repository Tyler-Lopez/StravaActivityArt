package com.company.athleteapiart.presentation.welcome_screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.welcome_screen.WelcomeScreenState.*
import com.company.athleteapiart.presentation.login_screen.LoginScreenViewModel
import com.company.athleteapiart.presentation.ui.theme.Lato
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(128.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(viewModel.athleteImageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(128.dp)
                        .clip(CircleShape)
                        .border(width = 5.dp, color = StravaOrange, shape = CircleShape)
                )
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "WELCOME",
                        fontFamily = Lato,
                    )
                    Text(
                        text = viewModel.athleteName.uppercase(),
                        fontFamily = Lato,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                // Navigate user to screen where they may select which years of activities to visualize
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(
                            Screen.TimeSelect.withArgs(
                                athleteId.toString(),
                                accessToken
                            )
                        )
                    }) {
                    Text(
                        text = "Make Activity Art",
                        fontFamily = Lato,
                        fontSize = 24.sp
                    )
                }
                // Navigates user to a simple screen showing information about app & author
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate(route = Screen.About.route) }) {
                    Text(
                        text = "About",
                        fontFamily = Lato,
                        fontSize = 24.sp
                    )
                }
                // De-authenticates the user and clears OAuth2 database entry
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.logout(context = context) }) {
                    Text(
                        text = "Logout",
                        fontFamily = Lato,
                        fontSize = 24.sp
                    )
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