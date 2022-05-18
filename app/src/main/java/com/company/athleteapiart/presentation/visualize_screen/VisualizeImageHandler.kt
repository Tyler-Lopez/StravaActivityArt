package com.company.athleteapiart.presentation.visualize_screen

import android.graphics.Bitmap
import androidx.compose.runtime.Composable

@Composable
fun VisualizeImageHandler(bitmap: Bitmap?) {
    bitmap?.let { VisualizeImage(it) }
}