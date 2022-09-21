package com.company.activityart.presentation.edit_art_screen.subscreens.preview

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
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
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LocalDensity.current.run {
            LaunchedEffect(
                keys = arrayOf(
                    filterStateWrapper.unixSecondSelectedEnd,
                    filterStateWrapper.unixSecondSelectedStart,
                    filterStateWrapper.excludedActivityTypes
                )
            ) {
                println("sending event to draw art")
                viewModel.onEvent(
                    EditArtPreviewViewEvent.DrawArtRequested(
                        targetHeightPx = sizeWrapper.heightPx,
                        targetWidthPx = sizeWrapper.widthPx,
                        screenWidthPx = maxWidth.toPx(),
                        screenHeightPx = maxHeight.toPx(),
                        excludeActivityTypes = filterStateWrapper.excludedActivityTypes,
                        unixSecondSelectedStart = filterStateWrapper.unixSecondSelectedStart,
                        unixSecondSelectedEnd = filterStateWrapper.unixSecondSelectedEnd
                    )
                )
            }
        }
        viewModel.viewState.collectAsState().value?.apply {
            when (this) {
                is Loading -> CircularProgressIndicator()
                is Standby -> EditArtPreviewStandby(bitmap = bitmap)
            }
        }
    }
}