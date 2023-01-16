package com.activityartapp.presentation.welcomeScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.common.errorScreen.ErrorScreen
import com.activityartapp.presentation.welcomeScreen.composables.WelcomeStandby
import com.activityartapp.util.classes.ApiError


/*

Welcome Screen
This is the screen users who are authenticated see first on opening the app.

https://developers.strava.com/guidelines/

 */

@Composable
fun WelcomeScreen(viewModel: WelcomeViewModel) {
    ScreenBackground {
        viewModel.viewState.collectAsState().value?.apply {
            when (this) {
                is WelcomeViewState.Standby -> WelcomeStandby(
                    athleteName = athleteName,
                    athleteImageUrl = athleteImageUrl,
                    eventReceiver = viewModel
                )
                is WelcomeViewState.Error -> {
                    /*
                    val headerDescription = when (error) {
                        ApiError.UserFacingError.AthleteRateLimited -> Pair(
                            stringResource(R.string.welcome_strava_athlete_rate_limited_header),
                            stringResource(R.string.welcome_strava_athlete_rate_limited_description)
                        )
                        ApiError.UserFacingError.NoInternet -> Pair(
                            stringResource(R.string.welcome_no_internet_header),
                            stringResource(R.string.welcome_no_internet_description)
                        )
                        ApiError.UserFacingError.StravaRateLimited -> Pair(
                            stringResource(R.string.welcome_strava_app_rate_limited_header),
                            stringResource(R.string.welcome_strava_app_rate_limited_description)
                        )
                        ApiError.UserFacingError.StravaServerIssues -> Pair(
                            stringResource(R.string.welcome_strava_server_issue_header),
                            stringResource(R.string.welcome_strava_server_issue_description)
                        )
                        ApiError.UserFacingError.Unknown -> Pair(
                            stringResource(R.string.welcome_strava_server_issue_header),
                            stringResource(R.string.welcome_strava_server_issue_description)
                        )
                    }
                    ErrorScreen(
                        header = headerDescription.first,
                        description = headerDescription.second,
                        retrying = retrying,
                        onRetryClicked = {
                            if (error is ApiError.UserFacingError.NoInternet || error is ApiError.UserFacingError.StravaServerIssues) {
                                viewModel.onEventDebounced(WelcomeViewEvent.ClickedRetryConnection)
                            }
                        }
                    )
                }

                     */
                }
            }
        }
    }
}
