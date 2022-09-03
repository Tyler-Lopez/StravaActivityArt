package com.company.activityart.presentation.about_screen

import android.view.View
import androidx.compose.foundation.ScrollState
import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed class AboutScreenViewEvent : ViewEvent {
    object NavigateUpClicked : AboutScreenViewEvent()
}

sealed class AboutScreenViewState : ViewState {
    object Standby : AboutScreenViewState()
}