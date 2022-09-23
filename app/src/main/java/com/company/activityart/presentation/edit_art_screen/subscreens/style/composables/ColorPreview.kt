package com.company.activityart.presentation.edit_art_screen.subscreens.style.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.company.activityart.R
import com.company.activityart.presentation.edit_art_screen.ColorWrapper

@Composable
fun ColorPreview(colorWrapper: ColorWrapper) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        drawRect(
            size = size,
            color = colorWrapper.run {
                Color(red, green, blue, alpha)
            }
        )
    }
}