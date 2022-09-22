package com.company.activityart.presentation.edit_art_screen.subscreens.style

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.company.activityart.presentation.edit_art_screen.ColorWrapper
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewState

sealed interface EditArtStyleViewEvent : ViewEvent {

}

sealed interface EditArtStyleViewState : ViewState {
    data class Standby(
        val colorBackground: ColorWrapper
    ) : EditArtStyleViewState
}