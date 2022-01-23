package com.company.athleteapiart.presentation.activity_visualize_screen

import android.graphics.*
import androidx.compose.ui.geometry.Offset
import com.company.athleteapiart.data.remote.responses.Activity
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.meterToMiles
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import kotlin.math.ceil
import kotlin.math.sqrt

fun activitiesVisualizeCanvas(
    maxWidth: Int,
    activities: List<Activity>
): Bitmap {

    val format = AthleteActivities.formatting
    val background = format.value.backgroundColor

    // Create a bitmap which is a scaled representation of a 3420x4320 image
    val desiredWidth = 3420f
    val desiredHeight = 4320f
    val ratioMultiplier = desiredHeight / desiredWidth
    // Determine height of image given width
    val maxHeight = (maxWidth * ratioMultiplier).toInt()

    // Create a bitmap which will be drawn on by canvas
    val bitmap = Bitmap.createBitmap(
        maxWidth,
        maxHeight,
        Bitmap.Config.ARGB_8888
    )
    // Create canvas to draw on

    val canvas = Canvas(bitmap)
    val center = Offset(
        x = canvas.width / 2f,
        y = canvas.height / 2f
    )
    // Draw canvas background
    val backgroundColor = Paint()
    backgroundColor.color = Color
        .rgb(background.red, background.green, background.blue)
    canvas.drawRect(
        Rect(
            0,
            0,
            maxWidth,
            maxHeight
        ),
        backgroundColor
    )

    // Determine how many rows are necessary
    val colCount = (ceil(sqrt(activities.size.toDouble()))).toInt()

    // Determine width of each activity
    val activityWidth = (maxWidth - (colCount * 25)) / colCount

    // Iterate through each activity, determining X and Y position
    var xOffset = 0f
    var yOffset = activityWidth + 25f
    var column = 0
    for (activity in activities) {
        val summaryPolyline = activity.map.summary_polyline
        if (activity.type != "Run") continue
        if (summaryPolyline == "null" || summaryPolyline == null) continue


        column++
        if (column == colCount) {
            xOffset = activityWidth + 25f
            yOffset += activityWidth + 25f
            column = 1
        } else {
            xOffset += activityWidth + 25f
        }
        println("activity width is $activityWidth")
        println("row count is $colCount")
        println("x offset is $xOffset")


        // Decode Polyline into a List<LatLng>
        val latLngList = PolyUtil.decode(summaryPolyline)

        var top = Double.MAX_VALUE.times(-1.0)
        var bottom = Double.MAX_VALUE
        var left = Double.MAX_VALUE
        var right = Double.MAX_VALUE.times(-1.0)

        val normalizedLatLngList = mutableListOf<LatLng>()
        for (latLng in latLngList) {
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
            val normalY = latLng.latitude.minus((top.plus(bottom)).div(2.0)).times(-1.0)
            normalizedLatLngList.add(LatLng(normalY, normalX))
        }

        val heightNorm = top.minus(bottom)
        val widthNorm = right.minus(left)
        val largestSide = if (heightNorm < widthNorm) widthNorm else heightNorm


        val multiplier = activityWidth.div(largestSide)

        val points = mutableListOf<Float>()

        for (normalLatLng in normalizedLatLngList) {
            // x
            points.add((normalLatLng.longitude.times(multiplier)).toFloat() + xOffset)
            //   y
            points.add((normalLatLng.latitude.times(multiplier)).toFloat() + yOffset)
        }

        val pointsPaint = Paint()

        val distance = activity.distance.meterToMiles()

        pointsPaint.color = when {
            distance <= 5 -> Color.RED
            distance <= 10 -> Color.rgb(255, 153, 51)
            distance <= 20 -> Color.YELLOW
            else -> Color.GREEN
        }
        pointsPaint.isAntiAlias = true
        pointsPaint.strokeCap = Paint.Cap.ROUND
        pointsPaint.style = Paint.Style.STROKE
        pointsPaint.strokeWidth = 3f

        val path = Path()
        path.setLastPoint(points[0], points[1])
        for (i in 2 until points.lastIndex step 2) {
            path.lineTo(points[i], points[i + 1])
        }
        canvas.drawPath(path, pointsPaint)
    }

    return bitmap
}