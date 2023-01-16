@file:OptIn(ExperimentalComposeUiApi::class)

package com.activityartapp.presentation.loadActivitiesScreen

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.data.remote.responses.ActivityResponse
import com.activityartapp.domain.models.Activity
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.common.errorScreen.ErrorScreen
import com.activityartapp.presentation.common.type.SubheadHeavy
import com.activityartapp.presentation.loadActivitiesScreen.LoadActivitiesViewState.*
import com.activityartapp.util.classes.ApiError
import com.activityartapp.util.classes.ApiError.UserFacingError.*

/**
 * The only screen in the app which handles loading [ActivityResponse] from
 * Remote. After this screen, all [Activity] selected for visualization will
 * be passed via. nav arguments.
 *
 * After [Activity] are successfully loaded, the athlete is automatically redirected
 * to make their art.
 *
 * If the [Activity] are unsuccessfully loaded, the athlete is prompted
 * to choose whether to continue or try again to load.
 */
@Composable
fun LoadActivitiesScreen(viewModel: LoadActivitiesViewModel) {
    viewModel.apply {
        ScreenBackground {
            viewState.collectAsState().value?.apply {
                when (this) {
                    is ErrorApi -> {
                        val headerDescription = when (error) {
                            AthleteRateLimited -> Pair(
                                stringResource(R.string.welcome_strava_athlete_rate_limited_header),
                                error.getPrompt(totalActivitiesLoaded)
                            )
                            NoInternet -> Pair(
                                stringResource(R.string.welcome_no_internet_header),
                                error.getPrompt(totalActivitiesLoaded)
                            )
                            StravaRateLimited -> Pair(
                                stringResource(R.string.welcome_strava_app_rate_limited_header),
                                error.getPrompt(totalActivitiesLoaded)
                            )
                            StravaServerIssues -> Pair(
                                stringResource(R.string.welcome_strava_server_issue_header),
                                error.getPrompt(totalActivitiesLoaded)
                            )
                            Unknown -> Pair(
                                stringResource(R.string.welcome_strava_server_issue_header),
                                error.getPrompt(totalActivitiesLoaded)
                            )
                        }
                        ErrorScreen(
                            header = headerDescription.first,
                            description = headerDescription.second,
                            prompt = error.getDescription(),
                            retrying = retrying,
                            onContinueClicked = totalActivitiesLoaded.takeIf { it > 0 }?.run {
                                {
                                    viewModel.onEventDebounced(LoadActivitiesViewEvent.ClickedContinue)

                                }
                            },
                            onRetryClicked = error.takeIf { it is NoInternet || it is StravaServerIssues }
                                ?.run {
                                    {
                                        viewModel.onEventDebounced(LoadActivitiesViewEvent.ClickedRetry)
                                    }
                                },
                            onReturnClicked = {
                                viewModel.onEventDebounced(LoadActivitiesViewEvent.ClickedReturn)
                            }
                        )
                    }
                    is ErrorNoActivities -> {
                        ErrorScreen(
                            header = stringResource(R.string.loading_activities_no_activities_header),
                            description = stringResource(R.string.loading_activities_no_activities_description),
                            prompt = stringResource(R.string.loading_activities_no_activities_prompt),
                            retrying = false,
                            onReturnClicked = {
                                viewModel.onEventDebounced(LoadActivitiesViewEvent.ClickedReturn)
                            }
                        )
                    }
                    is Loading -> {
                        CircularProgressIndicator()
                        SubheadHeavy(
                            text = totalActivitiesLoaded.takeIf { it > 0 }?.let {
                                pluralStringResource(
                                    id = R.plurals.loading_activities_count,
                                    count = it, it
                                )
                            } ?: stringResource(id = R.string.loading_activities_zero_count)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun ApiError.UserFacingError.getDescription(): String {
    return when (this) {
        AthleteRateLimited -> stringResource(R.string.loading_activities_athlete_rate_limited_description)
        NoInternet -> stringResource(R.string.loading_activities_no_internet_description)
        StravaRateLimited -> stringResource(R.string.loading_activities_app_rate_limited_description)
        StravaServerIssues -> stringResource(R.string.loading_activities_server_issue_description)
        Unknown -> stringResource(R.string.loading_activities_unknown_description)
    }
}

@Composable
private fun ApiError.UserFacingError.getPrompt(activityCount: Int): String {
    return activityCount.takeIf { it > 0 }?.let {
        when (this) {
            AthleteRateLimited -> pluralStringResource(
                id = R.plurals.loading_activities_athlete_rate_limited_prompt,
                count = it, it
            )
            NoInternet -> pluralStringResource(
                id = R.plurals.loading_activities_no_internet_prompt,
                count = it, it
            )
            StravaRateLimited -> pluralStringResource(
                id = R.plurals.loading_activities_app_rate_limited_prompt,
                count = it, it
            )
            StravaServerIssues -> pluralStringResource(
                id = R.plurals.loading_activities_server_issue_prompt,
                count = it, it
            )
            Unknown -> pluralStringResource(
                id = R.plurals.loading_activities_unknown_prompt,
                count = it, it
            )
        }
    } ?: run {
        when (this) {
            AthleteRateLimited -> stringResource(R.string.loading_activities_athlete_rate_limited_prompt_zero_count)
            NoInternet -> stringResource(R.string.loading_activities_no_internet_prompt_zero_count)
            StravaRateLimited -> stringResource(R.string.loading_activities_app_rate_limited_prompt_zero_count)
            StravaServerIssues -> stringResource(R.string.loading_activities_server_issue_prompt_zero_count)
            Unknown -> stringResource(R.string.loading_activities_unknown_prompt_zero_count)
        }
    }
}