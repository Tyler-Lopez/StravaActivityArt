package com.company.activityart.presentation.edit_art_screen.subscreens.preview

import androidx.annotation.Px
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewEvent.*

@Composable
fun EditArtPreviewStandby(
    unixSecondSelectedStart: Float,
    unixSecondSelectedEnd: Float,
    @Px targetHeightPx: Float,
    @Px targetWidthPx: Float,
    eventReceiver: EventReceiver<EditArtPreviewViewEvent>
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        LocalDensity.current.run {
            LaunchedEffect(
                keys = arrayOf(
                    maxHeight,
                    maxWidth,
                    unixSecondSelectedEnd,
                    unixSecondSelectedStart
                )
            ) {
                eventReceiver.onEvent(
                    DrawArtRequested(
                        targetHeightPx = targetHeightPx,
                        targetWidthPx = targetWidthPx,
                        screenWidthPx = maxWidth.toPx(),
                        screenHeightPx = maxHeight.toPx()
                    )
                )
            }
        }
    }
}