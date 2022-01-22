package com.company.athleteapiart.presentation.login_screen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.athleteapiart.R
import com.company.athleteapiart.presentation.composable.ComposableHeader


// https://developers.strava.com/guidelines/
@Composable
fun LoginScreen(
    onClick: (Intent?) -> Unit,
    // If login button is pushed, return intent
    viewModel: LoginScreenViewModel = hiltViewModel()
) {
    val isLoading by remember { viewModel.isLoading }
    val requestLogin by remember { viewModel.requestLogin }

    synchronized(LocalContext.current) {
        viewModel.loadDao(LocalContext.current)
    }

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
        ComposableHeader(
            text = "ACTIVITIES",
            modifier = Modifier.fillMaxWidth()
        )
        ComposableHeader(
            text = "VISUALIZER",
            isBold = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (!isLoading) {
            if (!requestLogin) {
                onClick(null)
            } else {
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
}