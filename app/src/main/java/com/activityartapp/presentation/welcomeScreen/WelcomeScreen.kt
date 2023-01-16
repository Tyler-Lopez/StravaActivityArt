package com.activityartapp.presentation.welcomeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.presentation.AppLogo
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.button.HighEmphasisButton
import com.activityartapp.presentation.common.button.LowEmphasisButton
import com.activityartapp.presentation.common.button.MediumEmphasisButton

/*

Welcome Screen
This is the screen users who are authenticated see first on opening the app.

https://developers.strava.com/guidelines/

 */
@Composable
fun WelcomeScreen(viewModel: WelcomeViewModel) {
    ScreenBackground {
        viewModel.viewState.collectAsState().value?.apply {
            AppLogo()
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HighEmphasisButton(
                    size = ButtonSize.LARGE,
                    text = stringResource(id = R.string.welcome_button_make_art),
                ) { viewModel.onEventDebounced(WelcomeViewEvent.ClickedMakeArt) }
                MediumEmphasisButton(
                    size = ButtonSize.LARGE,
                    text = stringResource(id = R.string.welcome_button_about),
                ) { viewModel.onEventDebounced(WelcomeViewEvent.ClickedAbout) }
                LowEmphasisButton(
                    size = ButtonSize.LARGE,
                    text = stringResource(id = R.string.welcome_button_logout),
                ) { viewModel.onEventDebounced(WelcomeViewEvent.ClickedLogout) }
            }
        }
    }
}