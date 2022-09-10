package com.company.activityart.presentation.make_art_screen

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.activityart.R
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.ScreenBackground
import com.company.activityart.presentation.make_art_screen.MakeArtViewEvent.*
import com.company.activityart.presentation.make_art_screen.MakeArtViewState.*
import com.company.activityart.presentation.load_activities_screen.LoadActivitiesViewEvent
import com.company.activityart.presentation.load_activities_screen.LoadActivitiesViewState
import com.company.activityart.presentation.load_activities_screen.composables.LoadActivitiesLoadError
import com.company.activityart.presentation.load_activities_screen.composables.LoadActivitiesLoading
import com.company.activityart.presentation.ui.theme.spacing

@Composable
fun MakeArtScreen(
    router: Router<MainDestination>,
    viewModel: MakeArtViewModel = hiltViewModel()
) {
    viewModel.apply {
        LaunchedEffect(key1 = router) { attachRouter(router) }
        AppBarScaffold(
            text = stringResource(R.string.action_bar_load_activities_header),
            onNavigateUp = { viewModel.onEventDebounced(NavigateUpClicked) }
        ) {
            ScreenBackground(spacedBy = spacing.medium) {
                viewState.collectAsState().value?.apply {
                    when (this) {
                        is Loading -> CircularProgressIndicator()
                        is Standby -> {}
                    }
                }
            }
        }
    }
}