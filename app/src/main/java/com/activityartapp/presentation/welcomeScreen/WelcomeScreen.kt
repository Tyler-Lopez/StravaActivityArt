package com.activityartapp.presentation.welcomeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.presentation.AppLogo
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.button.HighEmphasisButton
import com.activityartapp.presentation.common.button.LowEmphasisButton
import com.activityartapp.presentation.common.button.MediumEmphasisButton
import com.activityartapp.presentation.common.errorScreen.ErrorScreen
import com.activityartapp.presentation.common.type.Body
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.common.type.SubheadHeavy
import com.activityartapp.util.constants.StringConstants.APP_VERSION
import retrofit2.http.Body

/**
 * If an athlete is authenticated they are automatically routed to this screen.
 */
@Composable
fun WelcomeScreen(viewModel: WelcomeViewModel) {
    ScreenBackground {
        viewModel.viewState.collectAsState().value?.apply {
            when (this) {
                is WelcomeViewState.Loading -> CircularProgressIndicator()
                is WelcomeViewState.ErrorUnsupportedVersion -> ErrorScreen(
                    header = stringResource(R.string.error_unsupported_version_header),
                    description = stringResource(R.string.error_unsupported_version_description),
                    prompt = stringResource(R.string.error_unsupported_version_prompt)
                )
                is WelcomeViewState.Standby -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AppLogo()
                        SubheadHeavy(
                            text = stringResource(
                                id = R.string.app_version,
                                APP_VERSION
                            )
                        )
                        if (!versionIsLatest) {
                            SubheadHeavy(text = stringResource(R.string.welcome_new_version_available))
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterVertically
                        ),
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
    }
}