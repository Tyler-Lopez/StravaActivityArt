package com.company.athleteapiart.presentation.welcome_screen

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.welcome_screen.WelcomeScreenState.*
import com.company.athleteapiart.presentation.ui.theme.Lato
import com.company.athleteapiart.presentation.ui.theme.StravaOrange


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
        LOADING -> {
            //   Text("Loading")
        }
        STANDBY -> {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                val maxHeight = this.maxHeight
                val maxWidth = this.maxWidth

                Column(
                    modifier = Modifier.widthIn(360.dp, maxWidth * 0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(viewModel.athleteImageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                            .border(width = 8.dp, color = StravaOrange, shape = CircleShape)
                    )

                    Text(
                        text = "Welcome to Activity Art",
                        fontSize = 20.sp,
                        fontFamily = Lato,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = viewModel.athleteName.uppercase(),
                        fontFamily = Lato,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    // Navigate user to screen where they may select which years of activities to visualize
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            navController.navigate(
                                Screen.FilterYear.withArgs(
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
        }
        LOGOUT ->
            LaunchedEffect(Unit) {
                navController.navigate(route = Screen.Login.route) {
                    popUpTo(route = Screen.Welcome.route + "/{athleteId}/{accessToken}") {
                        inclusive = true
                    }
                }
            }

    }

}