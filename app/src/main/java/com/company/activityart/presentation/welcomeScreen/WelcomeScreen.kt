package com.company.activityart.presentation.welcomeScreen

import androidx.annotation.Dimension
import androidx.annotation.Px
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.company.activityart.R
import com.company.activityart.presentation.AppLogo
import com.company.activityart.presentation.common.button.ButtonSize
import com.company.activityart.presentation.common.button.HighEmphasisButton
import com.company.activityart.presentation.common.button.MediumEmphasisButton
import com.company.activityart.presentation.common.type.*
import com.company.activityart.presentation.common.ScreenBackground
import com.company.activityart.presentation.ui.theme.Silver
import com.company.activityart.presentation.welcomeScreen.composables.WelcomeNoInternet
import com.company.activityart.presentation.welcomeScreen.composables.WelcomeStandby


/*

Welcome Screen
This is the screen users who are authenticated see first on opening the app.

https://developers.strava.com/guidelines/

 */

@Composable
fun WelcomeScreen(viewModel: WelcomeViewModel) {
    ScreenBackground {
        AppLogo()
        viewModel.viewState.collectAsState().value?.apply {
            when (this) {
                is WelcomeViewState.Standby -> WelcomeStandby(
                    athleteName = athleteName,
                    athleteImageUrl = athleteImageUrl,
                    eventReceiver = viewModel
                )
                is WelcomeViewState.NoInternet -> WelcomeNoInternet(
                    retrying = retrying,
                    eventReceiver = viewModel
                )
            }
        }

    }
}
