package com.activityartapp.presentation.loginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.ui.theme.StravaOrange

/**
 * When the athlete is determined as unauthenticated,
 * this screen is the start destination.
 *
 * Presents a button which will prompt the athlete to
 * "Connect with Strava".
 */
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .weight(1f, false)
        ) {
            Image(
                painterResource(R.drawable.display_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.BottomCenter,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(StravaOrange))
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(painterResource(id = R.drawable.ic_activity_art_logo), contentDescription = null)
            Subhead(
                text = stringResource(id = R.string.app_description),
                modifier = Modifier.sizeIn(maxWidth = 254.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_btn_strava_connectwith_orange_clipped),
                contentDescription = stringResource(id = R.string.connect_with_strava_button_cd),
                modifier = Modifier
                    .clickable { viewModel.onEvent(LoginViewEvent.ConnectWithStravaClicked) },
            )
        }
    }
    /*
    ScreenBackground(
        padding = 0.dp,
        scrollingEnabled = false
    ) {
        viewModel.viewState.collectAsState().value?.apply {

            Image(painterResource(id = R.drawable.ic_activity_art_logo), contentDescription = null)
            Subhead(
                text = stringResource(id = R.string.app_description),
                modifier = Modifier.sizeIn(maxWidth = 254.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_btn_strava_connectwith_orange_clipped),
                contentDescription = stringResource(id = R.string.connect_with_strava_button_cd),
                modifier = Modifier
                    .clickable { viewModel.onEvent(LoginViewEvent.ConnectWithStravaClicked) },
            )
        }
    }

     */
}