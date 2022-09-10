package com.company.activityart.presentation.make_art_screen

import android.graphics.Bitmap
import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState

sealed class MakeArtViewEvent : ViewEvent {
    object MakeFullscreenClicked : MakeArtViewEvent()
    object NavigateUpClicked : MakeArtViewEvent()
    object SaveClicked : MakeArtViewEvent()
    object SelectFiltersClicked : MakeArtViewEvent()
    object SelectStylesClicked : MakeArtViewEvent()
}

sealed class MakeArtViewState : ViewState {
    object Loading : MakeArtViewState()
    data class Standby(val artPreview: Bitmap) : MakeArtViewState()

}