package com.company.activityart.presentation.edit_art_screen.subscreens.preview

import android.util.Size
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.company.activityart.presentation.edit_art_screen.ColorWrapper
import com.company.activityart.presentation.edit_art_screen.FilterStateWrapper
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewEvent.DrawArtRequested
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewState.Loading
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewState.Standby

@Composable
fun EditArtPreviewViewDelegate(
    filterStateWrapper: FilterStateWrapper,
    size: Size,
    styleBackground: ColorWrapper,
    viewModel: EditArtPreviewViewModel
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LocalDensity.current.apply {
            /** On any change to filters or size, redraw the
             * bitmap */
            LaunchedEffect(
                keys = arrayOf(
                    filterStateWrapper,
                    size,
                    styleBackground
                )
            ) {
                viewModel.onEvent(
                    DrawArtRequested(
                        targetSize = size,
                        screenWidth = maxWidth.toPx().toInt(),
                        screenHeight = maxHeight.toPx().toInt(),
                        filterExcludedTypes = filterStateWrapper.excludedActivityTypes,
                        filterUnixSecondStart = filterStateWrapper.unixSecondSelectedEnd,
                        filterUnixSecondEnd = filterStateWrapper.unixSecondSelectedStart,
                        styleBackground = styleBackground
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