package com.activityartapp.presentation.editArtScreen.subscreens.preview

import androidx.compose.foundation.gestures.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.positionChanged
import kotlin.math.PI
import kotlin.math.abs
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastFirstOrNull
import androidx.compose.ui.util.fastForEach

suspend fun PointerInputScope.detectZoomPanGesture(
    onZoom: (centroid: Offset, pan: Offset, zoom: Float) -> Unit,
    onPan: (pan: Offset, pressed: Boolean) -> Unit
) {
    forEachGesture {
        awaitPointerEventScope {
            var zoom = 1f
            var pan = Offset.Zero
            var pastTouchSlop = false
            val touchSlop = viewConfiguration.touchSlop

            val down = awaitFirstDown(requireUnconsumed = false)
            do {
                val event = awaitPointerEvent()
                val wasNotReleased = event.changes.fastAny { it.id == down.id }
                println("here  $wasNotReleased was not released")
                val canceled = event.changes.fastAny { it.isConsumed }
                if (!canceled) {
                    val zoomChange = event.calculateZoom()
                    val panChange = event.calculatePan()

                    if (!pastTouchSlop) {
                        zoom *= zoomChange
                        pan += panChange

                        val centroidSize = event.calculateCentroidSize(useCurrent = false)
                        val zoomMotion = abs(1 - zoom) * centroidSize
                        val panMotion = pan.getDistance()

                        if (zoomMotion > touchSlop ||
                            panMotion > touchSlop
                        ) {
                            pastTouchSlop = true
                        }
                    }

                    if (pastTouchSlop) {
                        val centroid = event.calculateCentroid(useCurrent = false)
                        if (zoomChange != 1f) {
                            onZoom(centroid, panChange, zoomChange)
                        } else if (panChange != Offset.Zero) {
                            onPan(panChange, event.changes.fastAny { it.id == down.id })
                        }
                        event.changes.fastForEach {
                            if (it.positionChanged()) {
                                it.consume()
                            }
                        }
                    }
                }
            } while (!canceled && event.changes.fastAny { it.pressed }.also {
                println("all was pressed? $it")
                })
        }
    }
}