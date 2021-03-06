package com.company.activityart.presentation.welcome_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Radar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.company.activityart.Screen
import com.company.activityart.presentation.common.*
import com.company.activityart.presentation.ui.theme.*
import com.company.activityart.presentation.welcome_screen.WelcomeScreenState.*


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
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Icicle),
        contentAlignment = Alignment.Center
    ) {
        ContainerColumn(maxWidth) {
            when (screenState) {
                LAUNCH -> SideEffect {
                    viewModel.getAthlete(
                        context = context,
                        athleteId = athleteId,
                        accessToken = accessToken
                    )
                }
                LOADING -> LoadingComposable()
                STANDBY -> {
                    Image(
                        painter = rememberAsyncImagePainter(viewModel.athleteImageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(156.dp)
                            .clip(CircleShape)
                            .border(width = 8.dp, color = StravaOrange, shape = CircleShape)
                    )
                    AppVersionNameComposable()
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Silver,
                    ) {
                        Text(
                            text = viewModel.athleteName,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            fontFamily = MaisonNeue,
                            color = Coal,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
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
                    ) { navController.navigate(route = Screen.About.route) }
                    // De-authenticates the user and clears OAuth2 database entry
                    ButtonComposable(
                        text = "Logout",
                        modifier = Modifier.fillMaxWidth()
                    ) { viewModel.logout(context = context) }
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
    }
}