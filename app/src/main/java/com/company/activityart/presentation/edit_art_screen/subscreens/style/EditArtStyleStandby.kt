package com.company.activityart.presentation.edit_art_screen.subscreens.style

import androidx.compose.runtime.Composable
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.edit_art_screen.ColorWrapper
import com.company.activityart.presentation.edit_art_screen.subscreens.style.composables.StyleSectionBackground

@Composable
fun EditArtStyleStandby(
    colorBackground: ColorWrapper,
    eventReceiver: EventReceiver<EditArtStyleViewEvent>
) {
    StyleSectionBackground(backgroundColor = colorBackground, eventReceiver = eventReceiver)

}