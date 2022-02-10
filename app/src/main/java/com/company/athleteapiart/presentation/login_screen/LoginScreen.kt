package com.company.athleteapiart.presentation.login_screen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
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
import com.company.athleteapiart.ui.spacing
import com.company.athleteapiart.ui.theme.StravaOrange


// https://developers.strava.com/guidelines/
@Composable
fun LoginScreen(
    onClick: (Intent?) -> Unit,
    // If login button is pushed, return intent
    viewModel: LoginScreenViewModel = hiltViewModel()
) {
    val isLoading by remember { viewModel.isLoading }
    val endReached by remember { viewModel.endReached }
    val requestLogin by remember { viewModel.requestLogin }

    if (!isLoading && !endReached)
    viewModel.loadDao(LocalContext.current)
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(id = R.drawable.ic_paint_brush_svgrepo_com),
            "",
            modifier = Modifier.size(300.dp).padding(MaterialTheme.spacing.md)
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
            modifier = Modifier.fillMaxWidth()
        )

        if (!isLoading && endReached) {
            if (!requestLogin) {
                onClick(null)
            } else {
                Image(
                    painter = painterResource(id = R.drawable.btn_strava_connectwith_orange),
                    contentDescription = "Connect with Strava",
                    modifier = Modifier
                        .width(250.dp)
                        .padding(MaterialTheme.spacing.md)
                        .clickable { onClick(viewModel.loginIntent) }
                )
            }
        }
    }
}