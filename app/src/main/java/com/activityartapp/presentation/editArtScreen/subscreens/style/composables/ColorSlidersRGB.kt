package com.activityartapp.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.foundation.layout.ColumnScope
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
        colorName = stringResource(
            R.string.edit_art_style_color_red,
            color.redAsEightBit
        ),
        enabled = enabled,
        colorValue = color.red,
        colorType = ColorType.RED,
        onColorChanged = onColorChanged
    )
    ColorSlider(
        colorName = stringResource(
            R.string.edit_art_style_color_green,
            color.greenAsEightBit
        ),
        enabled = enabled,
        colorValue = color.green,
        colorType = ColorType.GREEN,
        onColorChanged = onColorChanged
    )
    ColorSlider(
        colorName = stringResource(
            R.string.edit_art_style_color_blue,
            color.blueAsEightBit
        ),
        enabled = enabled,
        colorValue = color.blue,
        colorType = ColorType.BLUE,
        onColorChanged = onColorChanged
    )
}