package com.company.activityart.presentation.edit_art_screen.subscreens.preview

import android.graphics.Bitmap
import androidx.annotation.Px
import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewState

sealed interface EditArtPreviewViewEvent : ViewEvent {
    data class DrawArtRequested(
        @Px val targetWidthPx: Float,
        @Px val targetHeightPx: Float,
        @Px val screenWidthPx: Float,
        @Px val screenHeightPx: Float,
        val excludeActivityTypes: Set<String>,
        val unixSecondSelectedStart: Long,
        val unixSecondSelectedEnd: Long,
    ) : EditArtPreviewViewEvent
}

sealed interface EditArtPreviewViewState : ViewState {
    object Loading : EditArtPreviewViewState
    data class Standby(val bitmap: Bitmap?) : EditArtPreviewViewState
}