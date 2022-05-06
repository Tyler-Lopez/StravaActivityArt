package com.company.athleteapiart.presentation.login_screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.R
import com.company.athleteapiart.presentation.composable.ComposableHeader
import com.company.athleteapiart.presentation.ui.spacing
import com.company.athleteapiart.presentation.login_screen.LoginScreenState.*


// https://developers.strava.com/guidelines/
@Composable
fun LoginScreen(
    uri: Uri?,
    navController: NavHostController,
    onLoginIntent: (Intent) -> Unit,
    viewModel: LoginScreenViewModel = hiltViewModel()
) {
    val screenState by remember { viewModel.loginScreenState }
    val accessToken by remember { viewModel.accessToken }

    val context = LocalContext.current

    when (screenState) {
        // Just launched Login screen, check URI for access code
        // In future, check ROOM database for previous code
        LAUNCH -> {
            // SideEffect composable invoked on every recomposition
            // Necessary to ensure we recompose screenState
            SideEffect {
                viewModel.attemptGetAccessToken(
                    uri = uri,
                    context = context
                )
            }
        }
        // In process of trying to get response from Strava where we input in URI
        LOADING -> {
            // Loading Screen Composable goes here later
            Text("Loading state")
        }
        // Wait for user to press Login
        STANDBY -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource(id = R.drawable.ic_paint_brush_svgrepo_com),
                    "",
                    modifier = Modifier
                        .size(300.dp)
                        .padding(MaterialTheme.spacing.md)
                )
                ComposableHeader(
                    text = "ACTIVITIES",
                    isBold = true,
                    center = true,
                    modifier = Modifier.fillMaxWidth()
                )
                ComposableHeader(
                    text = "ART",
                    isBold = true,
                    center = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MaterialTheme.spacing.md)

                )

                Image(
                    painter = painterResource(id = R.drawable.btn_strava_connectwith_orange),
                    contentDescription = "Connect with Strava",
                    modifier = Modifier
                        .width(300.dp)
                        .clickable {
                            println("Here intent is ${viewModel.loginIntent}")
                            onLoginIntent(viewModel.loginIntent)
                        }
                )
            }
        }
        // User has been successfully authorized
        AUTHORIZED -> {
            Text(accessToken ?: "no access token")
          //  navController.navigate(Screen.TimeSelect.route)
        }
    }
}