package com.company.activityart.presentation.welcome_screen

sealed class WelcomeScreenViewEvent {
    data class LoadAthlete(
        val athleteId: Long,
        val accessToken: String
    ) : WelcomeScreenViewEvent()
}