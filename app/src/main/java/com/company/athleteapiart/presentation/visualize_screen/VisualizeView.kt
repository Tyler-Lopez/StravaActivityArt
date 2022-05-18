package com.company.athleteapiart.presentation.visualize_screen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewTreeObserver
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

        val view = ComposeView(ctx)
        view.visibility = View.GONE
        view.layoutParams = LayoutParams(width, height)
        this.addView(view)

        view.setContent(content = content)

        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val graphicUtils = GraphicUtils()
                val bitmap = graphicUtils.createBitmapFromView(view = view, width = width, height = height)
                onBitmapCreated(bitmap)
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

}