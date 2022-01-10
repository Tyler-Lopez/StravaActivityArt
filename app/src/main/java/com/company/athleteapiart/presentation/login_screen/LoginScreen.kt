package com.company.athleteapiart.presentation.login_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.R


// https://developers.strava.com/guidelines/
@Composable
fun LoginScreen(
    onClick: () -> (Unit)
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ACTIVITIES VISUALIZER",
            textAlign = TextAlign.Center,
            fontSize = 35.sp,
            letterSpacing = 0.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .width(250.dp)
        )
            Image(
                painter = painterResource(id = R.drawable.btn_strava_connectwith_orange),
                contentDescription = "Connect with Strava",
                modifier = Modifier
                    .width(250.dp)
                    .clickable { onClick() }
            )
    }
}