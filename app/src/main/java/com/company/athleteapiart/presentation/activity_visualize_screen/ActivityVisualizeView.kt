package com.company.athleteapiart.presentation.activity_visualize_screen

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.util.GraphicUtils
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil

// https://proandroiddev.com/create-bitmaps-from-jetpack-composables-bdb2c95db51
class ActivityVisualizeView(
    ctx: Context,
    // Will not work without val
    val summaryPolyline: String,
    onBitmapCreated: (Bitmap) -> Unit
) : LinearLayoutCompat(ctx) {
    init {

        val width = 3420
        val height = 4320

        val view = ComposeView(ctx)
        view.visibility = View.GONE
        view.layoutParams = LayoutParams(width, height)
        this.addView(view)

        view.setContent {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val maxWidth = this.maxWidth
                val maxHeight = this.maxHeight

                Canvas(
                    modifier = Modifier
                        .width(maxWidth)
                        .height(maxHeight)
                        .background(color = Color.DarkGray)
                ) {
                    val center = center

                    val latLngList = PolyUtil.decode(summaryPolyline)

                    var top = Double.MIN_VALUE
                    var bottom = Double.MAX_VALUE
                    var left = Double.MAX_VALUE
                    var right = Double.MIN_VALUE

                    val normalizedLatLngList = mutableListOf<LatLng>()
                    for (latLng in latLngList) {
                        val normalX = latLng.longitude.minus(latLngList[0].longitude)
                        val normalY = latLng.latitude.minus(latLngList[0].latitude)

                        // Determine bounds
                        if (normalY > top) top = normalY
                        if (normalY < bottom) bottom = normalY
                        if (normalX < left) left = normalX
                        if (normalX > right) right = normalX

                        normalizedLatLngList.add(LatLng(normalY, normalX))
                    }

                    val activityHeight = bottom - top
                    val activityWidth = right - left
                    val largestSide = if (activityHeight < activityWidth) activityWidth else activityHeight
                    val desiredWidth = 500f
                    val multiplier = desiredWidth/largestSide

                    val points = mutableListOf<Offset>()

                    for (normalLatLng in normalizedLatLngList) {
                        points.add(
                            Offset(
                                x = (normalLatLng.longitude.minus(activityWidth/2.0).times(multiplier)).toFloat() + center.x,
                                y = (normalLatLng.latitude.minus(activityHeight/2.0).times(multiplier)).toFloat() + center.y,
                            )
                        )

                    }


                    drawCircle(
                        color = Color.Blue,
                        center = center,
                        radius = 20f
                    )
                    drawPoints(
                        points = points,
                        pointMode = PointMode.Polygon,
                        color = Color.Magenta,
                        strokeWidth = 10f,
                        cap = StrokeCap.Round,
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