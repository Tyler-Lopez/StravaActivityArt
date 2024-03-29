package com.activityartapp.presentation.welcomeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.activityartapp.BuildConfig
import com.activityartapp.R
import com.activityartapp.presentation.AppLogo
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.common.button.Button
import com.activityartapp.presentation.common.button.ButtonEmphasis
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.layout.ColumnMediumSpacing
import com.activityartapp.presentation.common.layout.ColumnSmallSpacing
import com.activityartapp.presentation.ui.theme.spacing

/**
 * If an athlete is authenticated they are automatically routed to this screen.
 */
@Composable
fun WelcomeScreen(viewModel: WelcomeViewModel) {
    Box(modifier = Modifier.padding(spacing.medium)) {
        ScreenBackground {
            viewModel.viewState.collectAsState().value?.apply {
                when (this) {
                    is WelcomeViewState.Loading -> CircularProgressIndicator()
                    is WelcomeViewState.Standby -> {
                        Card {
                            ColumnMediumSpacing(modifier = Modifier.padding(spacing.medium)) {
                                ColumnSmallSpacing {
                                    AppLogo()
                                    Text(
                                        text = stringResource(
                                            id = R.string.app_version,
                                            BuildConfig.VERSION_NAME
                                        ),
                                        style = MaterialTheme.typography.subtitle2
                                    )
                                    if (!versionIsLatest) {
                                        Text(
                                            text = stringResource(R.string.welcome_new_version_available),
                                            style = MaterialTheme.typography.subtitle1
                                        )
                                    }
                                }
                                ColumnSmallSpacing {
                                    Button(
                                        emphasis = ButtonEmphasis.HIGH,
                                        size = ButtonSize.LARGE,
                                        text = stringResource(id = R.string.welcome_button_make_art)
                                    ) { viewModel.onEventDebounced(WelcomeViewEvent.ClickedMakeArt) }
                                    Button(
                                        emphasis = ButtonEmphasis.LOW,
                                        size = ButtonSize.LARGE,
                                        text = stringResource(id = R.string.welcome_button_logout),
                                    ) { viewModel.onEventDebounced(WelcomeViewEvent.ClickedLogout) }
                                }
                            }
                        }

                    }
                }
            }
        }
        Image(
            modifier = Modifier.align(Alignment.BottomStart),
            painter = painterResource(id = R.drawable.ic_api_logo_pwrdby_strava_stack_gray),
            contentDescription = stringResource(id = R.string.powered_by_strava)
        )
    }
}