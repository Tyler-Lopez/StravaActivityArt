package com.activityartapp.presentation.editArtScreen.subscreens.preview

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.ErrorComposable
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent

@Composable
fun EditArtPreview(
    atLeastOneActivitySelected: State<Boolean>,
    backgroundIsTransparent: State<Boolean>,
    bitmap: State<Bitmap?>,
    offset: State<Offset>,
    scale: State<Float>,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    if (!atLeastOneActivitySelected.value) {
        ScreenBackground {
            ErrorComposable(
                header = stringResource(R.string.edit_art_preview_activities_zero_count_header),
                description = stringResource(R.string.edit_art_preview_activities_zero_count_description),
                prompt = stringResource(R.string.edit_art_preview_activities_zero_count_prompt)
            )
        }
    } else {
        bitmap.value?.let {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectZoomPanGesture(
                            { centroid, pan, zoom ->
                                eventReceiver.onEvent(EditArtViewEvent.PreviewGestureZoom(
                                    centroid,
                                    pan,
                                    zoom
                                ))
                            },
                            { _, released -> println("here, drag released $released") }
                        )
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            translationX = -offset.value.x
                            translationY = -offset.value.y
                            scaleX = scale.value
                            scaleY = scale.value
                            transformOrigin = TransformOrigin(0f, 0f)
                        }
                ) {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = stringResource(R.string.edit_art_preview_image_content_description),
                        modifier = Modifier,
                        contentScale = ContentScale.Fit,
                    )
                }
            }
            /*
            if (backgroundIsTransparent.value) {
                Button(
                    emphasis = ButtonEmphasis.LOW,
                    text = "Why is there a checkered pattern?",
                    size = ButtonSize.SMALL
                ) { eventReceiver.onEvent(EditArtViewEvent.ClickedInfoCheckeredBackground) }
            }

             */
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        }
    }
}