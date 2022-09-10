package com.company.activityart.presentation.select_years_screen

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.activityart.R
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.select_years_screen.SelectYearsViewState.*
import com.company.activityart.presentation.select_years_screen.SelectYearsViewEvent.*
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.ScreenBackground
import com.company.activityart.presentation.select_years_screen.composables.FilterYearScreenStandby
import com.company.activityart.presentation.ui.theme.spacing

/*

 FilterYearScreen

 This screen takes the athleteId and accessToken to load all activities from the API into ROOM
 Upon loading all activities, the user may choose which years they would like to take into the next

 After this Screen, we should never call the API again in workflow

 */

@Composable
fun LoadActivitiesScreen(
    router: Router<MainDestination>,
    viewModel: SelectYearsViewModel = hiltViewModel()
) {
    viewModel.apply {
        LaunchedEffect(router) { attachRouter(router) }
        AppBarScaffold(
            text = stringResource(R.string.action_bar_select_years_header),
            onNavigateUp = { viewModel.onEventDebounced(NavigateUpClicked) }
        ) {
            ScreenBackground(
                spacedBy = spacing.medium,
                verticalAlignment = Alignment.Top
            ) {
                viewState.collectAsState().value?.apply {
                    when (this) {
                        is Loading -> CircularProgressIndicator()
                        is Standby -> FilterYearScreenStandby(
                            activitiesCountByYear = activitiesCountByYear,
                            eventReceiver = viewModel
                        )
                    }
                }
            }
        }
    }
}

