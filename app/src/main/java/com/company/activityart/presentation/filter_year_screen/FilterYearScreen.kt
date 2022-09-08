package com.company.activityart.presentation.filter_year_screen

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.activityart.R
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.filter_year_screen.FilterYearViewState.*
import com.company.activityart.presentation.filter_year_screen.FilterYearViewEvent.*
import com.company.activityart.presentation.about_screen.AboutScreenViewState
import com.company.activityart.presentation.about_screen.composables.AboutScreenStandby
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.ScreenBackground
import com.company.activityart.presentation.filter_year_screen.composables.FilterYearScreenStandby
import com.company.activityart.presentation.ui.theme.spacing

/*

 FilterYearScreen

 This screen takes the athleteId and accessToken to load all activities from the API into ROOM
 Upon loading all activities, the user may choose which years they would like to take into the next

 After this Screen, we should never call the API again in workflow

 */

@Composable
fun FilterYearScreen(
    router: Router<MainDestination>,
    viewModel: FilterYearViewModel = hiltViewModel()
) {
    viewModel.apply {
        attachRouter(router)
        AppBarScaffold(
            text = stringResource(R.string.action_bar_filter_year_header),
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
                            isLoadingActivities = isLoading,
                            activitiesByYear = activitiesByYear,
                            eventReceiver = viewModel
                        )
                    }
                }
            }
        }
    }
}
/*
val screenState by remember { viewModel.timeSelectScreenState }
val context = LocalContext.current
val selectedActivitiesCount by remember { viewModel.selectedActivitiesCount }

BoxWithConstraints(
    modifier = Modifier
        .fillMaxSize()
        .background(Icicle),
    contentAlignment = Alignment.Center
) {
    val maxHeight = this.maxHeight

    ContainerColumn(maxWidth) {

        when (screenState) {
            LAUNCH -> SideEffect {
                viewModel.loadActivities(
                    context = context,
                    athleteId = athleteId,
                    accessToken = accessToken
                )
            }
            LOADING, STANDBY, ERROR -> {

                HeaderWithEmphasisComposable(emphasized = "years")
                Table.TableComposable(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(0.dp, maxHeight * 0.6f),
                    columns = viewModel.columns.toList(),
                    rows = viewModel.rows.toList(),
                    onSelectIndex = {
                        viewModel.updateSelectedActivities(it)
                    },
                    selectionList = viewModel.selectedActivities,
                )

                if (screenState == LOADING)
                    LoadingComposable()

                ActivitiesCountComposable(count = selectedActivitiesCount)
                ButtonWithCountComposable(activitiesEmpty = selectedActivitiesCount == 0) {
                    navController.navigate(
                        Screen.FilterMonth.withArgs(
                            athleteId.toString(),
                            accessToken,
                            viewModel.selectedYearsNavArgs()
                        )
                    )
                }

                // If there is an error, allow user to try to load activities again
                if (screenState == ERROR) {
                    Button(onClick = {
                        viewModel.loadActivities(
                            context = context,
                            athleteId = athleteId,
                            accessToken = accessToken
                        )
                    }) {
                        Text("ERROR")
                    }
                }
            }
        }
    }
}

 */
