package com.activityartapp.presentation

import android.net.Uri
import androidx.annotation.Px
import com.activityartapp.architecture.Destination
import com.activityartapp.architecture.ViewEvent
import com.activityartapp.architecture.ViewState
import com.activityartapp.presentation.editArtScreen.StrokeWidthType
import com.activityartapp.presentation.errorScreen.ErrorScreenType
import com.activityartapp.util.enums.*

sealed interface MainViewState : ViewState {
    object Unauthenticated : MainViewState
    object Authenticated : MainViewState
}

sealed interface MainViewEvent : ViewEvent {
    data class LoadAuthentication(val uri: Uri?) : MainViewEvent
}

sealed interface MainDestination : Destination {
    object ConnectWithStrava : MainDestination
    object NavigateLogin : MainDestination
    object NavigateLoadActivities : MainDestination

    data class NavigateEditArt(val athleteId: Long) : MainDestination
    data class NavigateError(
        val clearNavigationHistory: Boolean,
        val errorScreenType: ErrorScreenType
    ) : MainDestination

    data class NavigateSaveArt(
        val activityTypes: List<SportType>,
        val athleteId: Long,
        val backgroundAngleType: AngleType,
        val backgroundType: BackgroundType,
        val backgroundColorsArgb: List<Int>,
        val colorActivitiesArgb: Int,
        val colorFontArgb: Int,
        val filterBeforeMs: Long,
        val filterAfterMs: Long,
        val filterDistanceLessThanMeters: Int,
        val filterDistanceMoreThanMeters: Int,
        @Px val sizeHeight: Float,
        @Px val sizeWidth: Float,
        val sortType: EditArtSortType,
        val sortDirectionType: EditArtSortDirectionType,
        val strokeWidthType: StrokeWidthType,
        val textLeft: String?,
        val textCenter: String?,
        val textRight: String?,
        val textFontAssetPath: String,
        val textFontSize: FontSizeType
    ) : MainDestination

    object NavigateUp : MainDestination
    data class ShareFile(val uri: Uri) : MainDestination
}