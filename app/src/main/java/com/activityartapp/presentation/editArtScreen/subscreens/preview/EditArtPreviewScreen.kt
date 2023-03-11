package com.activityartapp.presentation.editArtScreen.subscreens.preview

import android.graphics.Bitmap
import android.util.Size
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Velocity
import com.activityartapp.R
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.ErrorComposable
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import kotlinx.coroutines.launch

@Composable
fun EditArtPreview(
    atLeastOneActivitySelected: State<Boolean>,
    backgroundIsTransparent: State<Boolean>,
    bitmap: State<Bitmap?>,
    //  offset: State<Offset>,
    //   scale: State<Float>,
    //  velocity: State<Velocity>,
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
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val localDensity = LocalDensity.current
                val scale = remember { mutableStateOf(1f) }
                val velocityDecay: DecayAnimationSpec<Float> = exponentialDecay()
                val velocityTracker: VelocityTracker = VelocityTracker()

                val computeScaledExcess: () -> Offset = {
                    val scaledBitmapWidth = it.width * scale.value
                    val scaledBitmapHeight = it.height * scale.value
                    Offset(
                        x = scaledBitmapWidth - localDensity.run { maxWidth.toPx() },
                        y = scaledBitmapHeight - localDensity.run { maxHeight.toPx() }
                    )
                }

                val centerFromScaledExcess: () -> Offset = {
                    (computeScaledExcess() / 2f).run {
                        copy(
                            x = x.coerceAtMost(maximumValue = 0f),
                            y = y.coerceAtMost(maximumValue = 0f)
                        )
                    }
                }

                val animatableOffsetX = remember {
                    Animatable(initialValue = centerFromScaledExcess().x)
                }
                val animatableOffsetY = remember {
                    Animatable(initialValue = centerFromScaledExcess().y)
                }

                val scope = rememberCoroutineScope()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectZoomPanGesture(
                                onZoom = { centroid, pan, zoom ->
                                    val oldScale = scale.value
                                    scale.value = (oldScale * zoom).coerceIn(1f..5f)

                                    /* Compute a new offset which would perfectly center the image within the user's
                                    fingers; may be out-of-bounds. */
                                    val scaledPrevOffset = Offset(
                                        animatableOffsetX.value,
                                        animatableOffsetY.value
                                    ) / oldScale
                                    val scaledPrevCent = centroid / oldScale
                                    val scaledPrevPan = pan / oldScale
                                    val scaledNewCent = centroid / scale.value
                                    val requestedOffset =
                                        scaledPrevOffset + scaledPrevCent - scaledNewCent + scaledPrevPan
                                    val scaledRequestedOffset = requestedOffset * scale.value

                                    /* Compute the float range which the new offset must be coerced within */
                                    val scaledExcess = computeScaledExcess()

                                    /* Adjust offset within maximum range */
                                    val newOffset = scaledRequestedOffset// + offsetToCenter

                                    val halfX = (scaledExcess.x / 2f).coerceAtMost(0f)
                                    if (scaledExcess.x >= 0f) {
                                        animatableOffsetX.updateBounds(
                                            lowerBound = 0f,
                                            upperBound = scaledExcess.x
                                        )
                                    } else {
                                        animatableOffsetX.updateBounds(
                                            lowerBound = halfX,
                                            upperBound = halfX
                                        )
                                    }
                                    val halfY = (scaledExcess.y / 2f).coerceAtMost(0f)
                                    if (scaledExcess.y >= 0f) {
                                        animatableOffsetY.updateBounds(
                                            lowerBound = 0f,
                                            upperBound = scaledExcess.y
                                        )
                                    } else {
                                        animatableOffsetY.updateBounds(
                                            lowerBound = halfY,
                                            upperBound = halfY
                                        )
                                    }
                                    velocityTracker.resetTracking()

                                    scope.launch {
                                        animatableOffsetX.snapTo(targetValue = newOffset.x)
                                        animatableOffsetY.snapTo(targetValue = newOffset.y)
                                    }
                                },
                                onPan = { pan ->
                                    /* Compute the float range which the new offset must be coerced within */
                                    val scaledExcess = computeScaledExcess()
                                    val offsetToCenter = Offset(
                                        scaledExcess.x
                                            .div(2f)
                                            .coerceAtMost(0f),
                                        scaledExcess.y
                                            .div(2f)
                                            .coerceAtMost(0f)
                                    )

                                    val maxOffsetX: Float =
                                        scaledExcess.x.coerceAtLeast(minimumValue = 0f)
                                    val maxOffsetY: Float =
                                        scaledExcess.y.coerceAtLeast(minimumValue = 0f)

                                    /* Adjust offset within maximum range */
                                    val newOffsetX = animatableOffsetX.value - pan.x
                                    val newOffsetY = animatableOffsetY.value - pan.y


                                    println("new offset is $newOffsetX plus offset to center ${offsetToCenter.x}")
                                    velocityTracker.addPosition(
                                        System.currentTimeMillis(),
                                        Offset(newOffsetX, newOffsetY)
                                    )
                                    println("velocity tracker added position...")
                                    val velocity = velocityTracker.calculateVelocity()
                                    println("velocity was $velocity")
                                    scope.launch {
                                        animatableOffsetX.snapTo(newOffsetX)
                                        animatableOffsetY.snapTo(newOffsetY)
                                    }
                                },
                                onRelease = {
                                    val velocity = velocityTracker.calculateVelocity()
                                    velocityTracker.resetTracking()

                                    scope.launch {
                                        animatableOffsetX.animateDecay(velocity.x, velocityDecay)
                                    }
                                    scope.launch {
                                        animatableOffsetY.animateDecay(velocity.y, velocityDecay)
                                    }
                                }
                            )
                        }
                        .graphicsLayer {
                            translationX = -animatableOffsetX.value
                            translationY = -animatableOffsetY.value
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
