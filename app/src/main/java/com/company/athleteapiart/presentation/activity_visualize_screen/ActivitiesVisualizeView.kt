package com.company.athleteapiart.presentation.activity_visualize_screen

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.company.athleteapiart.data.remote.responses.Activity
import com.company.athleteapiart.presentation.ui.theme.*
import com.company.athleteapiart.util.GraphicUtils
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil

// https://proandroiddev.com/create-bitmaps-from-jetpack-composables-bdb2c95db51
// https://foso.github.io/Jetpack-Compose-Playground/viewinterop/androidview/
class ActivitiesVisualizeView(
    var ctx: Context,
    var activities: List<Activity>,
    onBitmapCreated: (Bitmap) -> Unit
) : LinearLayoutCompat(ctx) {

    init {
        val width = 3420
        val height = 4320
        val view = ComposeView(ctx)
        view.visibility = View.VISIBLE
        view.layoutParams = LayoutParams(width, height)
        this.addView(view)

        view.setContent {


            val divideFactor = 4320f / 3420f
            val maxWidth = 360.dp
            val maxHeight = maxWidth.times(divideFactor)

            val activitiesPerColumn = 8
            val desiredWidth =
                LocalDensity.current.run { maxWidth.toPx() - ((activitiesPerColumn) * 50f) } / activitiesPerColumn
            Canvas(
                modifier = Modifier
                    .width(maxWidth)
                    .height(maxHeight)
                    .drawBehind {
                        drawLine(
                            color = WarmGrey10,
                            start = Offset(
                                x = 0f,
                                y = -5f,
                            ),
                            end = Offset(
                                x = this.size.width,
                                y = -5f
                            ),
                            strokeWidth = 5f
                        )
                        drawLine(
                            color = WarmGrey10,
                            start = Offset(
                                x = 0f,
                                y = this.size.height + 5f,
                            ),
                            end = Offset(
                                x = this.size.width,
                                y = this.size.height + 5f,
                            ),
                            strokeWidth = 5f
                        )
                    }
                    .background(color = Color.Black)
            ) {
                val center = center

                var xOffset = 0f
                var yOffset = desiredWidth + 50f
                var rowCount = 0
                for (activity in activities) {

                    val summaryPolyline = activity.map.summary_polyline
                    if (summaryPolyline == "null" || summaryPolyline == null) continue

                    rowCount++
                    if (rowCount == activitiesPerColumn) {
                        xOffset = desiredWidth + 50f
                        yOffset += desiredWidth
                        rowCount = 1
                    } else {
                        xOffset += desiredWidth + 50f
                    }


                    val latLngList = PolyUtil.decode(summaryPolyline)

                    var top = Double.MAX_VALUE.times(-1.0)
                    var bottom = Double.MAX_VALUE
                    var left = Double.MAX_VALUE
                    var right = Double.MAX_VALUE.times(-1.0)

                    val normalizedLatLngList = mutableListOf<LatLng>()
                    for (latLng in latLngList) {
                        //    val normalX = latLng.longitude.minus(latLngList[0].longitude)
                        //    val normalY = latLng.latitude.minus(latLngList[0].latitude)
                        val lat = latLng.latitude
                        val lng = latLng.longitude
                        // Determine bounds
                        if (lat > top) top = lat
                        if (lat < bottom) bottom = lat
                        if (lng < left) left = lng
                        if (lng > right) right = lng
                    }

                    for (latLng in latLngList) {
                        val normalX = latLng.longitude.minus((left.plus(right)).div(2.0))
                        val normalY = latLng.latitude.minus((top.plus(bottom)).div(2.0))
                        normalizedLatLngList.add(LatLng(normalY, normalX))
                    }

                    val heightNorm = top.minus(bottom)
                    val widthNorm = right.minus(left)
                    val largestSide = if (heightNorm < widthNorm) widthNorm else heightNorm


                    val multiplier = desiredWidth.div(largestSide)

                    val points = mutableListOf<Offset>()

                    for (normalLatLng in normalizedLatLngList) {
                        points.add(
                            Offset(
                                x = (normalLatLng.longitude.times(multiplier)).toFloat() + xOffset,
                                y = (normalLatLng.latitude.times(multiplier)).toFloat() + yOffset,
                            )
                        )
                    }

                    drawPoints(
                        points = points,
                        pointMode = PointMode.Polygon,
                        color = Color(252, 97, 0),
                        strokeWidth = 5f,
                        cap = StrokeCap.Round,
                    )
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(
                            x = 0f,
                            y = maxHeight.times(0.9f).toPx()
                        ),
                        size = Size(
                            width = maxWidth.toPx(),
                            height = maxHeight.times(0.1f).toPx()
                        )
                    )
                }
            }
        }


        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val graphicUtils = GraphicUtils()
                val bitmap = graphicUtils.createBitmapFromView(
                    view = view,
                    width = width,
                    height = height,
                )
                onBitmapCreated(bitmap)
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

        })
    }
}

