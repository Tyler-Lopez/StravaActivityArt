package com.activityartapp.presentation.editArtScreen.subscreens.preview

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.ErrorComposable
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.common.button.Button
import com.activityartapp.presentation.common.button.ButtonEmphasis
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.layout.ColumnMediumSpacing
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent

@Composable
fun EditArtPreview(
    atLeastOneActivitySelected: Boolean,
    backgroundIsTransparent: Boolean,
    bitmap: Bitmap?,
    zoom: Float,
    offset: Offset,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    if (!atLeastOneActivitySelected) {
        ScreenBackground {
            ErrorComposable(
                header = stringResource(R.string.edit_art_preview_activities_zero_count_header),
                description = stringResource(R.string.edit_art_preview_activities_zero_count_description),
                prompt = stringResource(R.string.edit_art_preview_activities_zero_count_prompt)
            )
        }
    } else {
        //  ColumnMediumSpacing(modifier = Modifier.fillMaxSize()) {
        bitmap?.let {
            /*
            var zoom by remember { mutableStateOf(1f) }
            var offset by remember { mutableStateOf(Offset.Zero) }
            val scaledExcess = (it.width * zoom) - it.width
            println("Zoom is $zoom")
            println("Screen width is: ${it.width}")
            println("ScaledExcess: $scaledExcess")
            println("Width times zoom is: ${it.width * zoom}")
            println("Offset: $offset")
            val minimumOffset: Float = 0f
            val maximumOffset: Float = scaledExcess / zoom
     //       println("Offset Range: $offsetRange")
            val minScale = 1f
            val maxScale = 3f

             */
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = stringResource(R.string.edit_art_preview_image_content_description),
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures(
                            onGesture = { centroid, pan, gestureZoom, _ ->
                                eventReceiver.onEvent(
                                    EditArtViewEvent.PreviewGesture(
                                        centroid,
                                        pan,
                                        gestureZoom
                                    )
                                )
                            }
                        )
                    }
                    .graphicsLayer {
                        translationX = -offset.x * zoom
                        translationY = -offset.y * zoom
                        scaleX = zoom
                        scaleY = zoom
                        transformOrigin = TransformOrigin(0f, 0f)
                    },
                contentScale = ContentScale.Fit,
            )
            /*
            if (backgroundIsTransparent) {
                Button(
                    emphasis = ButtonEmphasis.LOW,
                    text = "Why is there a checkered pattern?",
                    size = ButtonSize.SMALL
                ) { eventReceiver.onEvent(EditArtViewEvent.ClickedInfoCheckeredBackground) }
            }

             */
        } ?: run {
            CircularProgressIndicator()
        }
    }
}