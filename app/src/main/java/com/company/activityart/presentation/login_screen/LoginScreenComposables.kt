package com.company.activityart.presentation.login_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.company.activityart.R
import com.company.activityart.architecture.ViewEventListener
import com.company.activityart.presentation.login_screen.LoginScreenViewEvent.*
import com.company.activityart.presentation.ui.theme.*
import com.company.activityart.util.Constants

@Composable
fun LoginScreenStandbyState(
    eventReceiver: ViewEventListener<LoginScreenViewEvent>
) {
    Column(modifier = Modifier.padding(spacing.medium)) {
        Text(
            text = "${Constants.STAGE.uppercase()} " + Constants.VERSION,
            fontSize = 18.sp,
            fontFamily = Lato,
            color = Gravel,
            textAlign = TextAlign.Center
        )
        Text(
            text = Constants.APP_NAME,
            fontSize = 32.sp,
            fontFamily = MaisonNeue,
            color = Asphalt,
            fontWeight = FontWeight.Black,
        )
    }
    Image(
        painter = painterResource(id = R.drawable.ic_btn_strava_connectwith_orange),
        contentDescription = stringResource(id = R.string.connect_with_strava_button_cd),
        modifier = Modifier
            //.width(stravaButtonWidth)
            //.height(stravaButtonHeight)
            //.clip(ClippedImageShape)
            .clickable { eventReceiver.onEvent(ConnectWithStravaClicked) },
    )
}
