package com.activityartapp.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.activityartapp.presentation.editArtScreen.ColorType
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun ColorSlider(
    color: ColorWrapper,
    colorType: ColorType,
    enabled: Boolean,
    onColorChanged: (Pair<ColorType, Float>) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(spacing.medium)) {
        Text(
            text = colorName,
            modifier = Modifier.padding(start = spacing.small),
            style = MaterialTheme.typography.subtitle2
        )
        Slider(
            value = color.colorFromType(colorType),
            enabled = enabled,
            valueRange = ColorWrapper.VALUE_RANGE,
            onValueChange = { onColorChanged(colorType to it) }
        )
    }
}

private fun ColorWrapper.textFieldValueFromType(colorType: ColorType): Int {
    return when (colorType) {
        ColorType.RED -> red
        ColorType.GREEN -> green
        ColorType.BLUE -> blue
        ColorType.ALPHA -> alpha
    }
}

private fun ColorWrapper.colorFromType(colorType: ColorType): Float {
    return when (colorType) {
        ColorType.RED -> red
        ColorType.GREEN -> green
        ColorType.BLUE -> blue
        ColorType.ALPHA -> alpha
    }
}