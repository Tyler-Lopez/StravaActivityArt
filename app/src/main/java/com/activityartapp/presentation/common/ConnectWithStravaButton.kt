package com.activityartapp.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.loginScreen.LoginViewEvent

@Composable
fun ConnectWithStravaButton(onClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.ic_btn_strava_connectwith_orange_clipped),
        contentDescription = stringResource(id = R.string.connect_with_strava_button_cd),
        modifier = Modifier
            .clickable { onClick() }
    )
}