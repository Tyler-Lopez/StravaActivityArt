package com.company.activityart.presentation.about_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.company.activityart.R
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.about_screen.AboutScreenViewEvent.NavigateUpClicked
import com.company.activityart.presentation.about_screen.AboutScreenViewState.Standby
import com.company.activityart.presentation.about_screen.composables.AboutScreenStandby
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.ScreenBackground
import com.company.activityart.presentation.ui.theme.spacing

@Composable
fun AboutScreen(
    router: Router<MainDestination>,
    viewModel: AboutScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(router) { viewModel.attachRouter(router) }
    AppBarScaffold(
        text = stringResource(R.string.action_bar_about_header),
        onNavigateUp = { viewModel.onEventDebounced(NavigateUpClicked) }
    ) {
        ScreenBackground(
            spacedBy = spacing.medium,
            verticalAlignment = Top
        ) {
            viewModel.viewState.collectAsState().value?.apply {
                when (this) {
                    is Standby -> AboutScreenStandby()
                }
            }
        }
    }
}