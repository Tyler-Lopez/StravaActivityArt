package com.activityartapp.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.activityartapp.R
import com.activityartapp.presentation.editArtScreen.ColorType
import com.activityartapp.presentation.editArtScreen.ColorWrapper

@Composable
fun ColorSlidersRGB(
    color: ColorWrapper,
    enabled: Boolean,
    onColorChanged: (Pair<ColorType, Float>) -> Unit
) {
    ColorSlider(
        colorType = ColorType.RED,
        color = color,
        enabled = enabled,
        onColorChanged = onColorChanged
    )
    ColorSlider(
        colorType = ColorType.GREEN,
        color = color,
        enabled = enabled,
        onColorChanged = onColorChanged
    )
    ColorSlider(
        colorType = ColorType.BLUE,
        color = color,
        enabled = enabled,
        onColorChanged = onColorChanged
    )
}