package com.company.activityart.presentation.welcome_screen

import androidx.navigation.NavController
import com.company.activityart.architecture.Event

sealed class WelcomeScreenViewEvent : Event {
    data class ClickedAbout(val navController: NavController) : WelcomeScreenViewEvent()
    data class ClickedMakeArt(val navController: NavController) : WelcomeScreenViewEvent()
    data class ClickedLogout(val navController: NavController) : WelcomeScreenViewEvent()
    data class LoadAthlete(
        val athleteId: Long,
        val accessToken: String,
    ) : WelcomeScreenViewEvent()
}

sealed class WelcomeScreenViewState {
    object Launch : WelcomeScreenViewState()
    data class LoadError(val message: String) : WelcomeScreenViewState()
    object Loading : WelcomeScreenViewState()
    data class Standby(
        val athleteName: String,
        val athleteImageUrl: String,
    ) : WelcomeScreenViewState()
}