package com.company.activityart.presentation.login_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.activityart.R
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.common.type.TitleOne
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.composables.ScreenBackground
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.util.constants.StringConstants

/**
 * When the athlete is determined as unauthenticated,
 * this screen is the start destination.
 *
 * Presents a button which will prompt the athlete to
 * "Connect with Strava".
 */
@Composable
fun LoginScreen(
    router: Router<MainDestination>,
    viewModel: LoginScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(router) { viewModel.attachRouter(router) }
    ScreenBackground {
        viewModel.viewState.collectAsState().value?.apply {
            TitleOne(text = stringResource(id = R.string.app_name))
            Subhead(text = stringResource(id = R.string.app_description))
            Image(
                painter = painterResource(id = R.drawable.ic_btn_strava_connectwith_orange_clipped),
                contentDescription = stringResource(id = R.string.connect_with_strava_button_cd),
                modifier = Modifier
                    .clickable { viewModel.onEvent(LoginScreenViewEvent.ConnectWithStravaClicked) },
            )
        }
    }
}