package com.company.activityart.presentation.aboutScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.presentation.AppLogo
import com.company.activityart.presentation.aboutScreen.AboutViewEvent.NavigateUpClicked
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.ScreenBackground
import com.company.activityart.presentation.common.type.Body
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.ui.theme.spacing

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
                SubheadHeavy(stringResource(id = R.string.app_version))
                stringArrayResource(id = R.array.about_screen_content).forEach {
                    Body(text = it, modifier = Modifier.padding(horizontal = spacing.medium))
                }
            }
        }
    }
}