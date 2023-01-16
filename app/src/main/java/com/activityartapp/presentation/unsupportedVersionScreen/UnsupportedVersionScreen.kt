package com.activityartapp.presentation.unsupportedVersionScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.common.errorScreen.ErrorScreen

@Composable
fun UnsupportedVersionScreen() {
    ScreenBackground {
        ErrorScreen(
            header = stringResource(R.string.error_unsupported_version_header),
            description = stringResource(R.string.error_unsupported_version_description),
            prompt = stringResource(R.string.error_unsupported_version_prompt)
        )
    }
}