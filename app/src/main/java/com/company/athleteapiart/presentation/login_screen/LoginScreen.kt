package com.company.athleteapiart.presentation.login_screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.R
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.composable.ComposableHeader
import com.company.athleteapiart.presentation.ui.spacing
import com.company.athleteapiart.presentation.login_screen.LoginScreenState.*


// https://developers.strava.com/guidelines/
@Composable
fun LoginScreen(
    //onClick: (Intent?) -> Unit,
    // If login button is pushed, return intent
    uri: Uri?,
    navController: NavHostController,
    viewModel: LoginScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val screenState by remember { viewModel.loginScreenState }

    when (screenState) {
        // Just launched Login screen, check URI for access code
        // In future, check ROOM database for previous code
        Launch -> {
            viewModel.handleUri(uri)
        }
        // In process of trying to get response from Strava where we input in URI
        Loading -> {
            // Loading Screen Composable goes here later
        }
        // Wait for user to press Login
        Standby -> {
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
                            viewModel.startLoginIntent(context)
                        }

                )
            }
        }
        // User has been successfully authorized
        Authorized -> {
            navController.navigate(Screen.TimeSelect.route)
        }
    }

    // val isLoading by remember { viewModel.isLoading }
    //  val endReached by remember { viewModel.endReached }
    //   val requestLogin by remember { viewModel.requestLogin }

    // if (!isLoading && !endReached)
    //    viewModel.loadDao(LocalContext.current)

    //     if (!isLoading && endReached) {
    //        if (!requestLogin) {
    //            onClick(null)
    //     } else {

    //    }
    //    }
}
}