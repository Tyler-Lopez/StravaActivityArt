package com.activityartapp.presentation.aboutScreen

import com.activityartapp.architecture.ViewEvent
import com.activityartapp.architecture.ViewState

sealed class AboutViewEvent : ViewEvent {
    object NavigateUpClicked : AboutViewEvent()
}

object AboutViewState : ViewState