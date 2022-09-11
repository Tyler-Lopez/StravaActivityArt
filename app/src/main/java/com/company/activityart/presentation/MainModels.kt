package com.company.activityart.presentation

import android.net.Uri
import com.company.activityart.architecture.Destination
import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed class MainViewState : ViewState {
    object LoadingAuthentication : MainViewState()
    object Unauthenticated : MainViewState()
    object Authenticated : MainViewState()
}

sealed class MainViewEvent : ViewEvent {
    data class LoadAuthentication(val uri: Uri?) : MainViewEvent()
}

sealed class MainDestination : Destination {
    object ConnectWithStrava : MainDestination()
    object NavigateAbout : MainDestination()
    object NavigateLogin : MainDestination()
    data class NavigateLoadActivities(
        val athleteId: Long,
        val accessToken: String
    ) : MainDestination()
    data class NavigateMakeArt(
        val athleteId: Long,
        val accessToken: String
    ) : MainDestination()
    object NavigateUp : MainDestination()
}