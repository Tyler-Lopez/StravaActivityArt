package com.company.athleteapiart.presentation.visualize_screen

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

fun visualizeBitmapMaker(
    visualizeSpecification: VisualizeSpecification?
): Bitmap =
    // Create a bitmap which will be drawn on by canvas and return
    Bitmap.createBitmap(
        visualizeSpecification?.visualizationWidth ?: 1920,
        visualizeSpecification?.visualizationHeight ?: 1080,
        Bitmap.Config.ARGB_8888
    ).also { bitmap ->


        Canvas(bitmap).also { canvas ->
            // Draw background
            canvas.drawRect(
                Rect(0, 0, canvas.width, canvas.height),
                visualizeSpecification?.backgroundPaint ?: Paint()
            )

            for (path in visualizeSpecification?.activities ?: listOf()) {
                canvas.drawPath(
                    path,
                    visualizeSpecification?.activityPaint ?: Paint()
                )
            }

        }
    }
