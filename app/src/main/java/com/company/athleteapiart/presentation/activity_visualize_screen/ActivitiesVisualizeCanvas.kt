package com.company.athleteapiart.presentation.activity_visualize_screen

import android.graphics.*
import androidx.compose.ui.geometry.Offset
import com.company.athleteapiart.data.remote.responses.Activity

fun activitiesVisualizeCanvas(
    maxWidth: Int,
    activities: List<Activity>
): Bitmap {

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
    canvas.drawRect(
        Rect(
            0,
            0,
            maxWidth,
            maxHeight
        ),
        backgroundColor
    )
    val circlePaint = Paint()
    circlePaint.color = Color.CYAN

    return bitmap
}