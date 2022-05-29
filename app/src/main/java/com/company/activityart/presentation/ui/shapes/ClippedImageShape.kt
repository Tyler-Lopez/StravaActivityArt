package com.company.activityart.presentation.ui.shapes

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

object ClippedImageShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = Outline.Rectangle(
        Rect(
            left = 8f,
            right = size.width - 8f,
            top = 8f,
            bottom = size.height - 8f
        )
    )
}