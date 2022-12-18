package com.company.activityart.presentation.saveArtScreen

import android.graphics.Bitmap
import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed interface SaveArtViewEvent : ViewEvent {
    object ClickedDownload : SaveArtViewEvent
    object ClickedNavigateUp : SaveArtViewEvent
    object ClickedShare : SaveArtViewEvent
}

sealed interface SaveArtViewState : ViewState {
    object Loading : SaveArtViewState
    data class Standby(
        val bitmap: Bitmap,
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

