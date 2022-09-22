package com.company.activityart.presentation.edit_art_screen.subscreens.style

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.edit_art_screen.ColorWrapper
import com.company.activityart.presentation.edit_art_screen.subscreens.style.composables.StyleSectionBackground
import com.company.activityart.presentation.ui.theme.spacing

@Composable
fun EditArtStyleStandby(
    colorBackground: ColorWrapper,
    eventReceiver: EventReceiver<EditArtStyleViewEvent>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(spacing.medium)
    ) {
        StyleSectionBackground(backgroundColor = colorBackground, eventReceiver = eventReceiver)
    }
}