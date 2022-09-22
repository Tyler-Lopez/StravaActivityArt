package com.company.activityart.presentation.edit_art_screen.subscreens.style.composables

import androidx.compose.runtime.Composable
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.edit_art_screen.ColorWrapper
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.Section
import com.company.activityart.presentation.edit_art_screen.subscreens.style.EditArtStyleViewEvent

@Composable
fun StyleSectionBackground(
    backgroundColor: ColorWrapper,
    eventReceiver: EventReceiver<EditArtStyleViewEvent>
) {
    Section(
        header = "Background",
        description = "Set background color"
    ) {

    }
}