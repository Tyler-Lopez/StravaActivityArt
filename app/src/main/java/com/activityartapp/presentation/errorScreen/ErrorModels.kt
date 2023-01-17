package com.activityartapp.presentation.errorScreen

import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.architecture.ViewEvent
import com.activityartapp.architecture.ViewState

sealed interface ErrorViewEvent : ViewEvent {
    object ClickedReturn : ErrorViewEvent
}

data class ErrorViewState(val error: ErrorScreenType) : ViewState

enum class ErrorScreenType(
    @StringRes val headerStrRes: Int,
    @StringRes val descriptionStrRes: Int,
    @StringRes val promptStrRes: Int,
    val returnEnabled: Boolean
) {
    PERMISSION_DENIED(
        headerStrRes = R.string.error_permission_denied_header,
        descriptionStrRes = R.string.error_permission_denied_description,
        promptStrRes = R.string.error_permission_denied_prompt,
        returnEnabled = true
    ),
    UNSUPPORTED_VERSION(
        headerStrRes = R.string.error_unsupported_version_header,
        descriptionStrRes = R.string.error_unsupported_version_description,
        promptStrRes = R.string.error_unsupported_version_prompt,
        returnEnabled = false
    );
}