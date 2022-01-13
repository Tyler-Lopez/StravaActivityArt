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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.util.GraphicUtils

// https://proandroiddev.com/create-bitmaps-from-jetpack-composables-bdb2c95db51
class ActivityVisualizeView(
    ctx: Context,
    onBitmapCreated: (Bitmap) -> Unit
) : LinearLayoutCompat(ctx)
{
    init {
        val width = 3420
        val height = 4320
        val view = ComposeView(ctx)
        view.visibility = View.GONE
        view.layoutParams = LayoutParams(width, height)
        this.addView(view)

        view.setContent {
            Canvas(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.DarkGray)
                .offset()
            ) {
                drawCircle(
                    color = Color.Magenta,
                    center = center,
                    radius = 500f
                )

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