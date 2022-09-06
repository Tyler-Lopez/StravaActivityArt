package com.company.activityart.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun AppVersionNameComposable() {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*
        Text(
            text = "${StringConstants.STAGE.uppercase()} " + StringConstants.VERSION,
            fontSize = 18.sp,
            fontFamily = Lato,
            color = Gravel,
            textAlign = TextAlign.Center
        )
        Text(
            text = StringConstants.APP_NAME,
            fontSize = 26.sp,
            fontFamily = Lato,
            color = Asphalt,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

         */
    }
}