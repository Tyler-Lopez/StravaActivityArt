package com.company.athleteapiart.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.presentation.ui.theme.Asphalt
import com.company.athleteapiart.presentation.ui.theme.Gravel
import com.company.athleteapiart.presentation.ui.theme.Lato
import com.company.athleteapiart.util.Constants

@Composable
fun AppVersionNameComposable() {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${Constants.STAGE.uppercase()} " + Constants.VERSION,
            fontSize = 18.sp,
            fontFamily = Lato,
            color = Gravel,
            textAlign = TextAlign.Center
        )
        Text(
            text = Constants.APP_NAME,
            fontSize = 26.sp,
            fontFamily = Lato,
            color = Asphalt,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}