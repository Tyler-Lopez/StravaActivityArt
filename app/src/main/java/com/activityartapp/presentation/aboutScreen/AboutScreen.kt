package com.activityartapp.presentation.aboutScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.AppLogo
import com.activityartapp.presentation.aboutScreen.AboutViewEvent.NavigateUpClicked
import com.activityartapp.presentation.common.AppBarScaffold
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun AboutScreen(
    viewModel: AboutViewModel
) {
    AppBarScaffold(
        text = stringResource(R.string.action_bar_about_header),
        onNavigateUp = { viewModel.onEventDebounced(NavigateUpClicked) }
    ) {
        ScreenBackground {
            viewModel.viewState.collectAsState().value?.apply {
                AppLogo()
                stringArrayResource(id = R.array.about_screen_content).forEach {
                    Text(
                        text = it,
                        modifier = Modifier.padding(horizontal = spacing.medium),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}