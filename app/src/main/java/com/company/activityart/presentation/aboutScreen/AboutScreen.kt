package com.company.activityart.presentation.aboutScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.presentation.aboutScreen.AboutViewEvent.NavigateUpClicked
import com.company.activityart.presentation.aboutScreen.AboutViewState.Standby
import com.company.activityart.presentation.aboutScreen.composables.AboutScreenStandby
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.ScreenBackgroundLegacy
import com.company.activityart.presentation.ui.theme.spacing

@Composable
fun AboutScreen(
    viewModel: AboutViewModel
) {
    AppBarScaffold(
        text = stringResource(R.string.action_bar_about_header),
        onNavigateUp = { viewModel.onEventDebounced(NavigateUpClicked) }
    ) {
        ScreenBackgroundLegacy(
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