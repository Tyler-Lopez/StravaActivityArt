package com.company.athleteapiart.presentation.visualize_screen

import android.graphics.*
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.util.meterToMiles
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import kotlin.math.*

fun visualizeBitmap(
    bitmapWidth: Int, // This will be the width of the generated bitmap in pixels
    heightWidthRatio: Float, // Height ratio relative to bitmap width
    backgroundPaint: Paint,
    activityPaint: Paint,
    activities: List<ActivityEntity>
): Bitmap =
    // Create a bitmap which will be drawn on by canvas and return
    Bitmap.createBitmap(
        bitmapWidth,
        // Determine height of image given width and ratio
        (bitmapWidth * heightWidthRatio).toInt(),
        Bitmap.Config.ARGB_8888
    ).also { bitmap ->
        // Create canvas to draw on
        Canvas(bitmap).also { canvas ->

            val width = canvas.width
            val height = canvas.height

            // Draw background
            canvas.drawRect(
                Rect(0, 0, width, height),
                backgroundPaint
            )

            val marginX = 0f
            val marginY = 0f

            // https://math.stackexchange.com/questions/466198/algorithm-to-get-the-maximum-size-of-n-squares-that-fit-into-a-rectangle-with-a
            val ratio = 1f / heightWidthRatio
            val n = activities.size

            // Number of activities multiplied by the
            var colCount: Float = sqrt(n * ratio).toFloat()
            println("Col count calculated other way is ${sqrt(n / heightWidthRatio)}")
            println("Row count calculated other way is ${sqrt(n / (1f / heightWidthRatio))}")
            var rowCount: Float = n / colCount
            println("row count is actually $rowCount")
            println("row count alt form is ${sqrt(n * heightWidthRatio)}")

            val activitySize = minOf(
                // # Rows from attempting to fill whole width
                height / ceil(n / ceil(sqrt(n / heightWidthRatio)).toInt().toFloat()),
                // # Cols from attempting to fill whole height
                width / ceil(n / ceil(sqrt(n * heightWidthRatio)).toInt().toFloat())
            )
            println("activity size was calculated as $activitySize")

            println("here first thing is " + height / ceil(n / ceil(sqrt(n / heightWidthRatio)).toInt().toFloat()))
            println("Here second is " + width / ceil(n / ceil(sqrt(n * heightWidthRatio)).toInt().toFloat()))
            // Find option to fill whole height
            var numRowsFromHeight = ceil(rowCount)
            var numColsFromHeight = ceil(n / numRowsFromHeight)

            println("here numcolsfromheight is  prefancy ${n / numRowsFromHeight}")

            println("num rows from height before loop is $numRowsFromHeight")
            while (numRowsFromHeight * ratio < numColsFromHeight) {
                numRowsFromHeight++
                numColsFromHeight = ceil(n / numRowsFromHeight)
            }
            println("OI num cols from rows is " + ceil(n / ceil(sqrt(n * heightWidthRatio)).toInt().toFloat()))
            println("num rows from height after loop is $numRowsFromHeight")

            val sizeFromHeight = height / numRowsFromHeight

            // Find option to fill whole width
            var numColsFromWidth = ceil(colCount)
            var numRowsFromWidth = ceil(n / numColsFromWidth)

            println("here numrowsfromheight is  prefancy ${n / numColsFromHeight}")

            println("num cols from width before loop is $numColsFromHeight")
            while (numColsFromWidth < ratio * numRowsFromWidth) {
                numColsFromWidth++
                numRowsFromWidth = ceil(n / numColsFromWidth)
            }
            println("num cols from width after loop is $numColsFromHeight")

            println("OI num cDDDDDols from rows is " + ceil(n / ceil(sqrt(n / heightWidthRatio)).toInt().toFloat()))


            val sizeFromWidth = width / numColsFromWidth

            println("Col Count: $colCount")
            println("Row Count: $rowCount")
            println("Ratio: $ratio")
            println("Num Rows From Height: $numRowsFromHeight")
            println("Num Cols From Height: $numColsFromHeight")
            println("Num Rows From Width: $numRowsFromWidth")
            println("Num Cols From Width: $numColsFromWidth")
            println("Size from width is $sizeFromWidth")
            println("Size from height is $sizeFromHeight")
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
            println("activity size normal way  is $activityWidth")
            // Iterate through each activity, determining X and Y position
            val initialX = (width - (activityWidth * colCount)) / 2
            val initialY = (height - (activityWidth * rowCount)) / 2
            println((height % activityWidth).toString() + " i remainedrrr")
            println("col count is $colCount and in that parentheses is ${(activityWidth * colCount)} but trying to produce that number other way is ")
            println("equal to ${width / activityWidth}")
            println("col count is $rowCount and in that parentheses is ${(activityWidth * rowCount)} but trying to produce that number other way is ")
            println("equal to ${height / activityWidth}")
            println("initial x is $initialX")
            println("initial y is $initialY")

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

                val distance = activity.activityDistance.meterToMiles()


                val path = Path()
                path.setLastPoint(points[0], points[1])
                for (i in 0 until points.lastIndex step 2) {
                    path.lineTo(points[i], points[i + 1])
                }

                canvas.drawPath(path,
                    activityPaint.also {
                        it.isAntiAlias = true
                        it.strokeCap = Paint.Cap.ROUND
                        it.style = Paint.Style.STROKE
                        it.strokeWidth = sqrt(activityWidth) * 0.1f
                    })
            }
        }
    }