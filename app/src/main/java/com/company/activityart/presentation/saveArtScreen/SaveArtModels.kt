package com.company.activityart.presentation.saveArtScreen

import android.graphics.Bitmap
import android.util.Size
import androidx.annotation.Px
import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent

sealed interface SaveArtViewEvent : ViewEvent {
    object ClickedDownload : SaveArtViewEvent
    object ClickedNavigateUp : SaveArtViewEvent
    object ClickedShare : SaveArtViewEvent
    data class ScreenMeasured(val size: Size) : SaveArtViewEvent
}

sealed interface SaveArtViewState : ViewState {
    object Loading : SaveArtViewState
    data class Standby(
        val bitmapDownloadSize: Bitmap,
        val bitmapScreenSize: Bitmap,
        val buttonsEnabled: Boolean = true,
        val downloadInProgress: Boolean = false
    ) : SaveArtViewState {

        fun copyDownloadStart(): Standby {
            return copy(
                buttonsEnabled = false,
                downloadInProgress = true
            )
        }

        fun copyDownloadTerminate(): Standby {
            return copy(
                buttonsEnabled = true,
                downloadInProgress = false
            )
        }
    }
}

