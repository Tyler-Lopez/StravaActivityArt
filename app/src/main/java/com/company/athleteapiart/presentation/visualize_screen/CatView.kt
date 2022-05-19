package com.company.athleteapiart.presentation.visualize_screen

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.R
import com.company.athleteapiart.util.GraphicUtils

class CatView(ctx: Context, onBitmapCreated: (bitmap: Bitmap) -> Unit) : LinearLayoutCompat(ctx) {

    init {
        val width = 1920
        val height = 1080

        val view = ComposeView(ctx)
        view.visibility = View.VISIBLE
        view.layoutParams = LayoutParams(width, height)
        this.addView(view)

        view.setContent {
            CatInfo()
        }

        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                println("on global layout")
                val graphicUtils = GraphicUtils()
                val bitmap = graphicUtils.createBitmapFromView(view = view, width = width, height = height)
                onBitmapCreated(bitmap)
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }
}

@Composable
fun CatInfo() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(Color.LightGray)) {


        Text("Trixy the Cat", fontSize = 14.sp)
    }
}