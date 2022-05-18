package com.company.athleteapiart.presentation.activity_visualize_screen

import android.content.Context
import android.graphics.*
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.domain.model.DistanceRule
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.meterToMiles
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import kotlin.math.*

fun activitiesVisualizeCanvas(
    maxWidth: Int,
    context: Context,
    activities: List<ActivityEntity>
): Bitmap {

    val format = AthleteActivities.formatting
    val conditions = format.value.conditions
 //   val background = format.value.backgroundColor
    val actColor = format.value.activityColor

    // Create a bitmap which is a scaled representation of a 3420x4320 image
    val desiredWidth = 3840f
    val desiredHeight = 2160f
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
    // Draw canvas background
    /*
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

     */


    val x = maxWidth.toFloat()
    val y = maxHeight.toFloat()
    val marginX = 0f
    val marginY = 0f
   // val x = maxWidth.times(0.8f)
  //  val y = maxHeight.times(if (true) 0.75f else 0.8f)

 //   val marginX = (maxWidth.times(0.2f)).div(2f)
  //  val marginY = (maxHeight.times(0.2f)).div(2f)

    // https://math.stackexchange.com/questions/466198/algorithm-to-get-the-maximum-size-of-n-squares-that-fit-into-a-rectangle-with-a
    val ratio = x / y
    val n = activities.size
    var colCount: Float = sqrt(n * ratio).toFloat()
    var rowCount: Float = n / colCount

    // Find option to fill whole height
    var numRowsFromHeight = ceil(rowCount)
    var numColsFromHeight = ceil(n / numRowsFromHeight)
    while (numRowsFromHeight * ratio < numColsFromHeight) {
        numRowsFromHeight++
        numColsFromHeight = ceil(n / numRowsFromHeight)
    }
    val sizeFromHeight = y / numRowsFromHeight

    // Find option to fill whole width
    var numColsFromWidth = ceil(colCount)
    var numRowsFromWidth = ceil(n / numColsFromWidth)
    while (numColsFromWidth < ratio * numRowsFromWidth) {
        numColsFromWidth++
        numRowsFromWidth = ceil(n / numColsFromWidth)
    }
    val sizeFromWidth = x / numColsFromWidth

    var activityWidth: Float
    if (sizeFromHeight < sizeFromWidth) {
        rowCount = numRowsFromWidth
        colCount = numColsFromWidth
        activityWidth = sizeFromWidth.toFloat()
    } else {
        rowCount = numRowsFromHeight
        colCount = numColsFromHeight
        activityWidth = sizeFromHeight.toFloat()
    }

    // Iterate through each activity, determining X and Y position
    val initialX = (x - (activityWidth * colCount)) / 2
    val initialY = (y - (activityWidth * rowCount)) / 2

    var xOffset = initialX + marginX
    var yOffset = initialY + marginY
    var column = colCount.toInt()

    var activityCount = 1
    var totalDistance = 0.0

    for (activity in activities) {
        totalDistance += activity.activityDistance
        val summaryPolyline = activity.summaryPolyline
        //if (activity.type != "Run") continue
        //  if (summaryPolyline == "null" || summaryPolyline == null) continue


        if (column == colCount.toInt()) {
            xOffset = initialX + marginX
            if (activityCount != 1) yOffset += activityWidth
            column = 0
        } else {
            xOffset += activityWidth
        }

        val blankSpaces = ((rowCount * colCount) - activities.size).toInt()

        if (activityCount - 1 == (activities.size - (colCount.toInt() - blankSpaces))) {
            xOffset += ((blankSpaces * activityWidth / 2))
        }

        activityCount++
        column++

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


        val multiplier = (activityWidth.times(0.8f)).div(largestSide)

        val points = mutableListOf<Float>()

        for (normalLatLng in normalizedLatLngList) {
            // x
            points.add((normalLatLng.longitude.times(multiplier)).toFloat() + xOffset + (activityWidth / 2f))
            //   y
            points.add((normalLatLng.latitude.times(multiplier)).toFloat() + yOffset + (activityWidth / 2f))
        }

        val pointsPaint = Paint()

        val distance = activity.activityDistance.meterToMiles()

        pointsPaint.color = Color.rgb(actColor.red, actColor.green, actColor.blue)


        // Are they any condition rules
        for (condition in conditions) {
            if (condition is DistanceRule) {
                if (condition.conditionMatched(distance as Comparable<Any>)) {
                    pointsPaint.color =
                        Color.rgb(condition.color.red, condition.color.green, condition.color.blue)
                    break
                }
            }
        }

        pointsPaint.isAntiAlias = true
        pointsPaint.strokeCap = Paint.Cap.ROUND
        pointsPaint.style = Paint.Style.STROKE
        pointsPaint.strokeWidth =
            sqrt(maxWidth.toDouble() * maxHeight.toDouble()).toFloat() * 0.0015f

        val path = Path()
        path.setLastPoint(points[0], points[1])
        for (i in 2 until points.lastIndex step 2) {
            path.lineTo(points[i], points[i + 1])
        }
        canvas.drawPath(path, pointsPaint)
    }

    /*

    // Place NAME in left side of screen
    val textSize = min(x, y) * 0.05f
    val textPaint = Paint()
    textPaint.textSize = textSize
    textPaint.color = Color.WHITE
    textPaint.typeface = Typeface.createFromAsset(context.assets, "maisonneue_demi.otf")
    textPaint.letterSpacing = 0.2f
    canvas.drawText(
        format.value.leftString,
        marginX,
        maxHeight.times(0.97f) - marginY + textSize,
        textPaint
    )
    textPaint.typeface = Typeface.createFromAsset(context.assets, "maisonneue_demi.otf")
    textPaint.color = Color.argb(100, 255, 255, 255)


    // Place YEAR in right side of screen
    val bounds = Rect()
    val rightText = "${totalDistance.meterToMiles().roundToInt()} mi | ${format.value.rightString}"
    textPaint.getTextBounds(rightText, 0, rightText.length, bounds)

    canvas.drawText(
        rightText,
        maxWidth - marginX - bounds.width(),
        maxHeight.times(0.97f) - marginY + textSize,
        textPaint
    )

     */

    return bitmap
}