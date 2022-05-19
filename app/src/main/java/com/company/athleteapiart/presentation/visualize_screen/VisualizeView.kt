package com.company.athleteapiart.presentation.visualize_screen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.ComposeView
import com.company.athleteapiart.util.GraphicUtils

// Create a custom view and add a ComposeView to it
@SuppressLint("ViewConstructor")
class VisualizeView(
    ctx: Context,
    height: Int,
    width: Int,
    onBitmapCreated: (Bitmap) -> Unit,
    content: @Composable () -> Unit
): LinearLayoutCompat(ctx) {


    init {
        val viewCtx = this
        println("here, init was invoked")
        val view = ComposeView(ctx)
        view.visibility = View.GONE
        view.layoutParams = LayoutParams(width, height)
        this.addView(view)
        view.setContent(content = content)

        viewTreeObserver.addOnDrawListener {
            println("on draw")
            /*
            val graphicUtils = GraphicUtils()
            val bitmap = graphicUtils.createBitmapFromView(
                view = view,
                width = width,
                height = height,
            )

             */
           // onBitmapCreated(bitmap)
         //   viewCtx.removeAllViews()
        }
     }

}