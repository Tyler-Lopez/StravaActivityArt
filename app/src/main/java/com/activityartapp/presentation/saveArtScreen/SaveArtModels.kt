package com.activityartapp.presentation.saveArtScreen

import android.graphics.Bitmap
import android.util.Size
import androidx.annotation.StringRes
import com.activityartapp.R
import com.activityartapp.architecture.ViewEvent
import com.activityartapp.architecture.ViewState

sealed interface SaveArtViewEvent : ViewEvent {
    object ActivityResumed : SaveArtViewEvent
    object ClickedDownload : SaveArtViewEvent
    object ClickedDownloadWhenPermissionPermaDenied : SaveArtViewEvent
    object ClickedNavigateUp : SaveArtViewEvent
    object ClickedShare : SaveArtViewEvent
    object ReceivedDownloadFailure : SaveArtViewEvent
    object ReceivedDownloadSuccess : SaveArtViewEvent
    object ReceivedShareFailure : SaveArtViewEvent
    object ReceivedShareSuccess : SaveArtViewEvent
    data class ScreenMeasured(val size: Size) : SaveArtViewEvent
}

sealed interface SaveArtViewState : ViewState {
    object Loading : SaveArtViewState
    data class Standby(
        val bitmapScreenSize: Bitmap,
        val downloadShareStatusType: DownloadShareStatusType = DownloadShareStatusType.STANDBY
    ) : SaveArtViewState {

        enum class DownloadShareStatusType(@StringRes val snackbarStringRes: Int?) {
            DOWNLOAD_FAILURE(R.string.save_art_snackbar_download_failure),
            DOWNLOAD_IN_PROGRESS(null),
            DOWNLOAD_SUCCESS(R.string.save_art_snackbar_download_success),
            SHARE_FAILURE(R.string.save_art_snackbar_share_failure),
            SHARE_IN_PROGRESS(null),
            SHARE_SUCCESS(null),
            SHARE_WAITING_FOR_RETURN(null),
            STANDBY(null)
        }
    }
}
