package com.company.activityart.presentation.select_years_screen

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.activityart.R
import com.company.activityart.architecture.Router
import com.company.activityart.data.remote.responses.ActivityResponse
import com.company.activityart.domain.models.Activity
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.select_years_screen.LoadActivitiesViewState.*
import com.company.activityart.presentation.select_years_screen.LoadActivitiesViewEvent.*
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.ScreenBackground
import com.company.activityart.presentation.select_years_screen.composables.LoadActivitiesLoadError
import com.company.activityart.presentation.select_years_screen.composables.LoadActivitiesLoading
import com.company.activityart.presentation.ui.theme.spacing

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
fun LoadActivitiesScreen(
    router: Router<MainDestination>,
    viewModel: LoadActivitiesViewModel = hiltViewModel()
) {
    viewModel.apply {
        LaunchedEffect(key1 = router) { attachRouter(router) }
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
                        is LoadError -> LoadActivitiesLoadError(
                            activitiesLoaded = totalActivitiesLoaded,
                            eventReceiver = viewModel
                        )
                        is Loading -> LoadActivitiesLoading(totalActivitiesLoaded)
                    }
                }
            }
        }
    }
}