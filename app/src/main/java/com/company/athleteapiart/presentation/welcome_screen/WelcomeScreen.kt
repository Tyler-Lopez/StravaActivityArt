package com.company.athleteapiart.presentation.welcome_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.common.ButtonComposable
import com.company.athleteapiart.presentation.ui.theme.*
import com.company.athleteapiart.presentation.welcome_screen.WelcomeScreenState.*


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
                modifier = Modifier
                    .fillMaxSize()
                    .background(Icicle),
                contentAlignment = Alignment.Center
            ) {

                val maxHeight = this.maxHeight
                val maxWidth = this.maxWidth

                Column(
                    modifier = Modifier.widthIn(240.dp, maxWidth * 0.75f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(viewModel.athleteImageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(156.dp)
                            .clip(CircleShape)
                            .border(width = 8.dp, color = StravaOrange, shape = CircleShape)
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ALPHA 0.1.0",
                            fontSize = 16.sp,
                            fontFamily = Lato,
                            color = Gravel,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Activity Art",
                            fontSize = 26.sp,
                            fontFamily = Lato,
                            color = Asphalt,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Silver,
                    ) {
                        Text(
                            text = viewModel.athleteName.uppercase(),
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            fontFamily = MaisonNeue,
                            color = Coal,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Navigate user to screen where they may select which years of activities to visualize
                    ButtonComposable(
                        text = "Make Art",
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        navController.navigate(
                            Screen.FilterYear.withArgs(
                                athleteId.toString(),
                                accessToken
                            )
                        )
                    }
                    // Navigates user to a simple screen showing information about app & author
                    ButtonComposable(
                        text = "About",
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        navController.navigate(route = Screen.About.route)
                    }
                    // De-authenticates the user and clears OAuth2 database entry
                    ButtonComposable(
                        text = "Logout",
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.logout(context = context)
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