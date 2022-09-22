package com.company.activityart.presentation.edit_art_screen.subscreens.style

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun EditArtStyleViewDelegate(viewModel: EditArtStyleViewModel) {
    viewModel.viewState.collectAsState().value?.apply {
        EditArtStyleStandby(
            colorBackground = colorBackground,
            eventReceiver = viewModel
        )
    }
}