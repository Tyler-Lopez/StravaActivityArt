package com.activityartapp.presentation.loginScreen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.presentation.common.ConnectWithStravaButton
import com.activityartapp.presentation.common.type.Subhead

/**
 * When the athlete is determined as unauthenticated,
 * this screen is the start destination.
 *
 * Presents a button which will prompt the athlete to
 * "Connect with Strava".
 */
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    var orientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }

    val configuration = LocalConfiguration.current

    // If our configuration changes then this will launch a new coroutine scope for it
    LaunchedEffect(configuration) {
        // Save any changes to the orientation value on the configuration object
        snapshotFlow { configuration.orientation }
            .collect { orientation = it }
    }

    when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            LandscapeContent(viewModel)
        }
        else -> {
            PortraitContent(viewModel)
        }
    }
}


@Composable
fun LandscapeContent(viewModel: LoginViewModel) {
    Row(
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
                alignment = Alignment.Center,
                contentDescription = stringResource(R.string.content_description_feature_graphic)
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
             //   .background(StravaOrange)
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_activity_art_logo),
                contentDescription = stringResource(R.string.app_logo_content_description)
            )
            Subhead(
                text = stringResource(id = R.string.app_description),
                modifier = Modifier.sizeIn(maxWidth = 254.dp)
            )
            ConnectWithStravaButton {
                viewModel.onEvent(LoginViewEvent.ConnectWithStravaClicked)
            }
        }
    }
}

@Composable
fun PortraitContent(viewModel: LoginViewModel) {
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
                contentDescription = stringResource(R.string.content_description_feature_graphic)
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
           //     .background(StravaOrange)
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_activity_art_logo),
                contentDescription = stringResource(R.string.app_logo_content_description)
            )
            Subhead(
                text = stringResource(id = R.string.app_description),
                modifier = Modifier.sizeIn(maxWidth = 254.dp)
            )
            ConnectWithStravaButton {
                viewModel.onEvent(LoginViewEvent.ConnectWithStravaClicked)
            }
        }
    }
}