package com.company.athleteapiart.presentation.login_screen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.company.athleteapiart.R
import com.company.athleteapiart.presentation.time_select_screen.TimeSelectViewModel
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.Oauth2


// https://developers.strava.com/guidelines/
@Composable
fun LoginScreen(
    navController: NavController,
    // If login button is pushed, return intent
    onClick: (Intent) -> (Unit),
    viewModel: LoginScreenViewModel = hiltViewModel()
) {
    val isLoading by remember { viewModel.isLoading }
    val requestLogin by remember { viewModel.requestLogin }

    viewModel.loadDao(LocalContext.current)

    if (isLoading) {
        Text("Loading app")
    } else if (!requestLogin) {
        navController.navigate("time_select_screen")
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.logo
                ),
                contentDescription = "App Logo",
                modifier = Modifier
                    .width(250.dp)
                    .padding(bottom = 10.dp)
            )
            Text(
                text = "ACTIVITIES",
                textAlign = TextAlign.Center,
                fontSize = 35.sp,
                letterSpacing = 1.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .width(250.dp)
            )
            Text(
                text = "VISUALIZER",
                textAlign = TextAlign.Center,
                fontSize = 35.sp,
                letterSpacing = 1.sp,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .width(250.dp)
                    .padding(bottom = 10.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.btn_strava_connectwith_orange),
                contentDescription = "Connect with Strava",
                modifier = Modifier
                    .width(250.dp)
                    .clickable { onClick(viewModel.loginIntent) }
            )
        }
    }
}