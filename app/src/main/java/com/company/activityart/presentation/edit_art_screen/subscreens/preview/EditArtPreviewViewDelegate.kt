package com.company.activityart.presentation.edit_art_screen.subscreens.preview

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.company.activityart.presentation.edit_art_screen.FilterStateWrapper
import com.company.activityart.presentation.edit_art_screen.SizeWrapper
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersStandby
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewState.*

@Composable
fun EditArtPreviewViewDelegate(
    filterStateWrapper: FilterStateWrapper,
    sizeWrapper: SizeWrapper,
    viewModel: EditArtPreviewViewModel
) {
    viewModel.viewState.collectAsState().value?.apply {
        when (this) {
            is Loading -> CircularProgressIndicator()
            is Standby -> EditArtPreviewStandby(
                bitmap = bitmap,
                targetHeightPx = sizeWrapper.heightPx,
                targetWidthPx = sizeWrapper.widthPx,
                unixSecondSelectedEnd = filterStateWrapper.unixSecondSelectedEnd,
                unixSecondSelectedStart = filterStateWrapper.unixSecondSelectedStart,
                eventReceiver = viewModel
            )
        }
    }
}