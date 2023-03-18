package com.activityartapp.presentation.editArtScreen.subscreens.preview

import android.graphics.Bitmap
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.presentation.common.ErrorComposable
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.util.ImageSizeUtils
import kotlinx.coroutines.launch

@Composable
fun EditArtPreview(
    atLeastOneActivitySelected: State<Boolean>,
    bitmap: State<Bitmap?>,
    bitmapZoomedIn: State<Bitmap?>,
    desiredSize: Size,
    onPreviewGestureZoom: (EditArtViewEvent.PreviewGestureZoom) -> Unit
) {
    val imageSizeUtils = ImageSizeUtils()

    if (!atLeastOneActivitySelected.value) {
        ScreenBackground {
            ErrorComposable(
                header = stringResource(R.string.edit_art_preview_activities_zero_count_header),
                description = stringResource(R.string.edit_art_preview_activities_zero_count_description),
                prompt = stringResource(R.string.edit_art_preview_activities_zero_count_prompt)
            )
        }
    } else {
        bitmap.value?.let { bitmapValue ->
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val localDensity = LocalDensity.current

                val adjustedWidthDp = remember { mutableStateOf(0.dp) }
                val adjustedHeightDp = remember { mutableStateOf(0.dp) }

                val scale = remember { mutableStateOf(1f) }

                val velocityDecay: DecayAnimationSpec<Float> = exponentialDecay()
                val velocityTracker = VelocityTracker()

                val computeScaledSize: () -> Size = {
                    Size(
                        width = localDensity.run { adjustedWidthDp.value.toPx() } * scale.value,
                        height = localDensity.run { adjustedHeightDp.value.toPx() } * scale.value,
                    )
                }

                val computeScaledExcess: () -> Offset = {
                    val scaledSize = computeScaledSize()
                    Offset(
                        x = scaledSize.width - localDensity.run { maxWidth.toPx() },
                        y = scaledSize.height - localDensity.run { maxHeight.toPx() }
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

                val animatableOffsetX =
                    remember { Animatable(initialValue = centerFromScaledExcess().x) }
                val animatableOffsetY = remember {
                    Animatable(initialValue = centerFromScaledExcess().y)
                }

                val updateBounds: () -> Unit = {
                    /* Compute the float range which the new offset must be coerced within */
                    val scaledExcess = computeScaledExcess()
                    val forcedOffsetX = scaledExcess.x.takeIf { it < 0f }?.div(other = 2f)
                    val forcedOffsetY = scaledExcess.y.takeIf { it < 0f }?.div(other = 2f)
                    animatableOffsetX.updateBounds(
                        lowerBound = forcedOffsetX ?: 0f,
                        upperBound = forcedOffsetX ?: scaledExcess.x
                    )
                    animatableOffsetY.updateBounds(
                        lowerBound = forcedOffsetY ?: 0f,
                        upperBound = forcedOffsetY ?: scaledExcess.y
                    )
                }

                LaunchedEffect(key1 = maxWidth, key2 = maxHeight) {
                    val screenWidthAsPx = localDensity.run { maxWidth.toPx() }
                    val screenHeightAsPx = localDensity.run { maxHeight.toPx() }
                    val adjustedSize = imageSizeUtils.sizeToMaximumSize(
                        actualSize = desiredSize,
                        maximumSize = Size(screenWidthAsPx, screenHeightAsPx)
                    )
                    adjustedWidthDp.value = localDensity.run { adjustedSize.width.toDp() }
                    adjustedHeightDp.value = localDensity.run { adjustedSize.height.toDp() }
                    scale.value = 1f
                    updateBounds()
                }

                updateBounds()
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

                                    updateBounds()
                                    velocityTracker.resetTracking()

                                    scope.launch {
                                        animatableOffsetX.snapTo(targetValue = scaledRequestedOffset.x)
                                        animatableOffsetY.snapTo(targetValue = scaledRequestedOffset.y)
                                    }

                                    /*
                                    val scaledSize = computeScaledSize()

                                    // todo figure out
                                    onPreviewGestureZoom(
                                        EditArtViewEvent.PreviewGestureZoom(
                                            newScale = scale.value,
                                            visibleFractionLeft = scaledRequestedOffset.x / scaledExcess.x,
                                            visibleFractionBottom = scaledRequestedOffset.y / scaledExcess.y,
                                            visibleFractionTop = scaledRequestedOffset.x

                                        )
                                    )

                                     */

                                },
                                onPan = { pan ->
                                    /* Adjust offset within maximum range */
                                    val newOffsetX = animatableOffsetX.value - pan.x
                                    val newOffsetY = animatableOffsetY.value - pan.y

                                    velocityTracker.addPosition(
                                        System.currentTimeMillis(),
                                        Offset(newOffsetX, newOffsetY)
                                    )

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

                ) {
                    Image(
                        bitmap = bitmapValue.asImageBitmap(),
                        contentDescription = stringResource(R.string.edit_art_preview_image_content_description),
                        modifier = Modifier
                            .size(
                                width = adjustedWidthDp.value,
                                height = adjustedHeightDp.value
                            )
                            .graphicsLayer {
                                translationX = -animatableOffsetX.value
                                translationY = -animatableOffsetY.value
                                scaleX = scale.value
                                scaleY = scale.value
                                transformOrigin = TransformOrigin(0f, 0f)
                            },
                        contentScale = ContentScale.Crop,
                    )
                    if (scale.value > 1f) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            bitmapZoomedIn.value?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = stringResource(R.string.edit_art_preview_image_content_description)
                                )
                            } ?: CircularProgressIndicator()
                        }
                    }
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

    }
}
