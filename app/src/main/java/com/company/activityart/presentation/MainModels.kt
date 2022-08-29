package com.company.activityart.presentation

import android.content.Intent
import com.company.activityart.architecture.Destination
import com.company.activityart.architecture.ViewEvent

sealed class MainViewEvent : ViewEvent {
    data class ParseIntent(val intent: Intent) : MainViewEvent()
}

sealed class MainDestination : Destination {
    data class NavigateToWelcome(val intent: Intent) : MainDestination()
    object NavigateAbout : MainDestination()
    object NavigateLogin : MainDestination()
}