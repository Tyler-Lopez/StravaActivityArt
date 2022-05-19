package com.company.athleteapiart.presentation.visualize_screen

import android.graphics.Bitmap
import androidx.compose.runtime.Composable

@Composable
fun VisualizeImageHandler(bitmap: Bitmap?) {
    println("here bitmap is $bitmap")
    bitmap?.let { VisualizeImage(it) }
}