package com.company.activityart.presentation.aboutScreen

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed class AboutViewEvent : ViewEvent {
    object NavigateUpClicked : AboutViewEvent()
}

object AboutViewState : ViewState