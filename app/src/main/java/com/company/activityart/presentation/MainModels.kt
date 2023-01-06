package com.company.activityart.presentation

import android.graphics.Bitmap
import android.net.Uri
import com.company.activityart.architecture.Destination
import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.company.activityart.presentation.editArtScreen.StrokeWidthType
import com.company.activityart.util.FontSizeType
import com.company.activityart.util.FontType

sealed interface MainViewState : ViewState {
    object LoadingAuthentication : MainViewState
    object Unauthenticated : MainViewState
    data class Authenticated(
        val athleteId: Long,
        val accessToken: String
    ) : MainViewState
}

sealed interface MainViewEvent : ViewEvent {
    data class LoadAuthentication(val uri: Uri?) : MainViewEvent
}

sealed interface MainDestination : Destination {
    object ConnectWithStrava : MainDestination
    object NavigateAbout : MainDestination
    object NavigateLogin : MainDestination
    data class NavigateLoadActivities(
        val athleteId: String,
        val accessToken: String
    ) : MainDestination

    data class NavigateEditArt(val fromLoad: Boolean = true) : MainDestination
    data class NavigateSaveArt(
        val activityTypes: List<String>,
        val colorActivitiesArgb: Int,
        val colorBackgroundArgb: Int,
        val filterBeforeMs: Long,
        val filterAfterMs: Long,
        val filterDistanceLessThan: Double,
        val filterDistanceMoreThan: Double,
        val sizeHeightPx: Int,
        val sizeWidthPx: Int,
        val strokeWidthType: StrokeWidthType,
        val textLeft: String?,
        val textCenter: String?,
        val textRight: String?,
        val textFont: FontType,
        val textFontSize: FontSizeType
    ) : MainDestination

    object NavigateUp : MainDestination
    data class ShareFile(val uri: Uri) : MainDestination
}