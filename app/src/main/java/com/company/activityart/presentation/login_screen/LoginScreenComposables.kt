package com.company.activityart.presentation.login_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.company.activityart.R
import com.company.activityart.architecture.ViewEventListener
import com.company.activityart.presentation.login_screen.LoginScreenViewEvent.*
import com.company.activityart.presentation.ui.theme.Asphalt
import com.company.activityart.presentation.ui.theme.Gravel
import com.company.activityart.presentation.ui.theme.Lato
import com.company.activityart.presentation.ui.theme.MaisonNeue
import com.company.activityart.util.Constants

@Composable
fun LoginScreenStandbyState(
    eventReceiver: ViewEventListener<LoginScreenViewEvent>
) {
    Image(
        painterResource(id = R.drawable.ic_frameactivityart),
        "",
        modifier = Modifier,
        contentScale = ContentScale.FillWidth
    )
    Column(horizontalAlignment = Alignment

        .CenterHorizontally) {
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
        painter = painterResource(id = R.drawable.btn_strava_connectwith_orange),
        contentDescription = "Connect with Strava",
        modifier = Modifier
            //.width(stravaButtonWidth)
            //.height(stravaButtonHeight)
            //.clip(ClippedImageShape)
            .clickable { eventReceiver.onEvent(ConnectWithStravaClicked) },
        contentScale = ContentScale.FillBounds
    )
}
