package com.activityartapp.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat

// https://github.com/JohannBlake/bitmap-from-composable/blob/main/app/src/main/java/dev/wirespec/sample/utils/GraphicUtils.kt
class GraphicUtils {

    fun createBitmapFromView(view: View, width: Int, height: Int): Bitmap {
        view.layoutParams = LinearLayoutCompat.LayoutParams(
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )

        view.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        )

        view.layout(0, 0, width, height)

        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        canvas.setBitmap(bitmap)
        view.draw(canvas)

        return bitmap
    }
}