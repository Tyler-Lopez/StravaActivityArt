package com.activityartapp.presentation.saveArtScreen

import android.graphics.Bitmap
import android.util.Size
import androidx.compose.material.SnackbarHostState
import com.activityartapp.architecture.ViewEvent
import com.activityartapp.architecture.ViewState

sealed interface SaveArtViewEvent : ViewEvent {
    object ActivityResumed : SaveArtViewEvent
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
        val downloadInProgress: Boolean = false,
        val shareInProgress: Boolean = false,
        val snackbarHostState: SnackbarHostState
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

        fun copyShareStart(): Standby {
            return copy(
                buttonsEnabled = false,
                shareInProgress = true
            )
        }

        fun copyShareTerminate(): Standby {
            return copy(
                buttonsEnabled = true,
                shareInProgress = false
            )
        }
    }
}

