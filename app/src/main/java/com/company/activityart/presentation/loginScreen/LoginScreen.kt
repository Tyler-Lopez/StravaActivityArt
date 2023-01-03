package com.company.activityart.presentation.loginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.company.activityart.R
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.common.type.TitleOne
import com.company.activityart.presentation.editArtScreen.subscreens.filters.composables.ScreenBackground

/**
 * When the athlete is determined as unauthenticated,
 * this screen is the start destination.
 *
 * Presents a button which will prompt the athlete to
 * "Connect with Strava".
 */
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    ScreenBackground {
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
}