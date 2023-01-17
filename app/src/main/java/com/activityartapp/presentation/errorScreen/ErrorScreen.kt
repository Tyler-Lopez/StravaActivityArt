package com.activityartapp.presentation.errorScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.activityartapp.presentation.common.ErrorScreen
import com.activityartapp.presentation.common.ScreenBackground

@Composable
fun ErrorScreen(viewModel: ErrorViewModel) {
    ScreenBackground {
        viewModel.viewState.collectAsState().value?.apply {
            ErrorScreen(
                header = stringResource(error.headerStrRes),
                description = stringResource(error.descriptionStrRes),
                prompt = stringResource(error.promptStrRes),
                onReturnClicked = error.returnEnabled.takeIf { it }?.run {
                    { viewModel.onEventDebounced(ErrorViewEvent.ClickedReturn) }
                }
            )
        }
    }
}