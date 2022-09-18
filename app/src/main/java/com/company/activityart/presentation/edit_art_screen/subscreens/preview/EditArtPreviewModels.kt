package com.company.activityart.presentation.edit_art_screen.subscreens.preview

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewState

sealed interface EditArtPreviewViewEvent : ViewEvent {

}

sealed interface EditArtPreviewViewState : ViewState {
    object Loading : EditArtPreviewViewState
    object Standby : EditArtPreviewViewState
}