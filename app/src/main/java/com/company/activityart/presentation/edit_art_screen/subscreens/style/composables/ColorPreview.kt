package com.company.activityart.presentation.edit_art_screen.subscreens.style.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.company.activityart.presentation.edit_art_screen.ColorWrapper

@Composable
fun ColorPreview(colorWrapper: ColorWrapper) {
    Canvas(modifier = Modifier.width(100.dp).height(200.dp)) {
        drawRect(
            size = size,
            color = colorWrapper.run {
                Color(red, green, blue, alpha)
            }
        )
    }
}