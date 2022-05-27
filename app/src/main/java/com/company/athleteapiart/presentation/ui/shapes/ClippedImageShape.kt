package com.company.athleteapiart.presentation.ui.shapes

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
            left = size.width * .045f,
            right = size.width - (size.width * .040f),
            top = size.height * .070f,
            bottom = size.height - (size.height * .075f)
        )
    )
}