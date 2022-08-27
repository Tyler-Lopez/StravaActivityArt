package com.company.activityart.presentation.welcome_screen

sealed class WelcomeScreenViewState {
    object Launch : WelcomeScreenViewState()
    object Loading : WelcomeScreenViewState()
    data class Standby(
        val athleteName: String,
        val athleteImageUrl: String
    ) : WelcomeScreenViewState()
    object Logout : WelcomeScreenViewState()
}