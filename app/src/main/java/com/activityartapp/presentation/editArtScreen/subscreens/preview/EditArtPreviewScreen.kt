package com.activityartapp.presentation.editArtScreen.subscreens.preview

import android.graphics.Bitmap
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import com.activityartapp.architecture.EventReceiver
import com.activityartapp.presentation.common.ErrorComposable
import com.activityartapp.presentation.common.ScreenBackground
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.PreviewZoom
import com.activityartapp.presentation.ui.theme.spacing
import com.activityartapp.util.ImageSizeUtils
import com.activityartapp.util.ext.halve
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@Composable
fun EditArtPreview(
    atLeastOneActivitySelected: State<Boolean>,
    bitmap: State<Bitmap?>,
    bitmapZoomedIn: State<Bitmap?>,
    desiredSize: Size,
    eventReceiver: EventReceiver<EditArtViewEvent>
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
                    val forcedOffsetX = scaledExcess.x.takeIf { it < 0f }?.halve()
                    val forcedOffsetY = scaledExcess.y.takeIf { it < 0f }?.halve()
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
                                    val scaleOld = scale.value
                                    val scaleNew = (scaleOld * zoom).coerceIn(1f..5f)
                                    if (scaleOld == scaleNew) return@detectZoomPanGesture
                                    scale.value = scaleNew

                                    /* Compute a new offset which would perfectly center the image within the user's
                                    fingers; may be out-of-bounds. */
                                    val scaledPrevOffset = Offset(
                                        animatableOffsetX.value,
                                        animatableOffsetY.value
                                    ) / scaleOld
                                    val scaledPrevCent = centroid / scaleOld
                                    val scaledPrevPan = pan / scaleOld
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

                                    eventReceiver.onEvent(PreviewZoom(scale.value))
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
                                        val moveX = async {
                                            animatableOffsetX.animateDecay(
                                                initialVelocity = velocity.x,
                                                animationSpec = velocityDecay
                                            )
                                        }
                                        val moveY = async {
                                            animatableOffsetY.animateDecay(
                                                initialVelocity = velocity.y,
                                                animationSpec = velocityDecay
                                            )
                                        }
                                        awaitAll(moveX, moveY)
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

                                val actualWidth =
                                    localDensity.run { ((it.width) * scale.value).toDp() }
                                val actualHeight =
                                    localDensity.run { ((it.height) * scale.value).toDp() }
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentScale = ContentScale.None,
                                    contentDescription = stringResource(R.string.edit_art_preview_image_content_description),
                                    alignment = Alignment.Center,
                                    modifier = Modifier
                                        .requiredSize(
                                            width = actualWidth,
                                            height = actualHeight
                                        )
                                        .graphicsLayer {
                                            val excess = computeScaledExcess()
                                            val shiftX = excess.x.halve()
                                            val shiftY = excess.y.halve()
                                            translationX = -animatableOffsetX.value + shiftX
                                            translationY = -animatableOffsetY.value + shiftY
                                        }
                                )
                            } ?: Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(spacing.medium),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Card {
                                    Column(
                                        modifier = Modifier.padding(all = spacing.medium),
                                        verticalArrangement = Arrangement.spacedBy(space = spacing.medium),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = stringResource(R.string.edit_art_preview_loading_enhance),
                                            style = MaterialTheme.typography.body2
                                        )
                                        LinearProgressIndicator()
                                    }
                                }
                            }
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
