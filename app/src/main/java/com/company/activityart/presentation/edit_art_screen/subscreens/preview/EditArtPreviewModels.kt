package com.company.activityart.presentation.edit_art_screen.subscreens.preview

import android.graphics.Bitmap
import android.util.Size
import androidx.annotation.Px
import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.company.activityart.presentation.edit_art_screen.ColorWrapper
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewState

sealed interface EditArtPreviewViewEvent : ViewEvent {
    data class DrawArtRequested(
        @Px val targetSize: Size,
        @Px val screenWidth: Int,
        @Px val screenHeight: Int,
        val filterExcludedTypes: Set<String>,
        val filterUnixSecondEnd: Long,
        val filterUnixSecondStart: Long,
        val styleActivities: ColorWrapper,
        val styleBackground: ColorWrapper,
        ) : EditArtPreviewViewEvent
}

sealed interface EditArtPreviewViewState : ViewState {
    object Loading : EditArtPreviewViewState
    data class Standby(val bitmap: Bitmap?) : EditArtPreviewViewState
}