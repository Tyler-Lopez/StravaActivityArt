package com.company.activityart.presentation.edit_art_screen.subscreens.style

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.company.activityart.presentation.edit_art_screen.ColorWrapper

sealed interface EditArtStyleViewEvent : ViewEvent {
    data class ColorChanged(
        val styleType: StyleType,
        val colorType: ColorType,
        val changedTo: Float
    ) : EditArtStyleViewEvent
}

data class EditArtStyleViewState(
    val colorBackground: ColorWrapper
) : ViewState


enum class StyleType {
    BACKGROUND
}

enum class ColorType {
    RED,
    GREEN,
    BLUE,
    ALPHA
}