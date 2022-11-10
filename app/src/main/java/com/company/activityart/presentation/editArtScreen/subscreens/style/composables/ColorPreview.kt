package com.company.activityart.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import com.company.activityart.R
import com.company.activityart.presentation.editArtScreen.ColorWrapper

@Composable
fun ColorPreview(colorWrapper: ColorWrapper) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.color_preview_height))
            .border(
                width = dimensionResource(R.dimen.color_preview_stroke_width),
                color = colorResource(R.color.n30_silver)
            )
    ) {
        drawRect(
            size = size,
            color = colorWrapper.run {
                Color(red, green, blue, alpha)
            }
        )
    }
}