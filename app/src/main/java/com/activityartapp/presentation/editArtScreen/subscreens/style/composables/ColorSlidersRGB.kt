package com.activityartapp.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.activityartapp.R
import com.activityartapp.presentation.editArtScreen.ColorType
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.presentation.editArtScreen.composables.TextFieldSliderSpecification
import com.activityartapp.presentation.editArtScreen.composables.TextFieldSliders
import kotlin.math.roundToInt

@Composable
fun ColorSlidersRGB(
    color: ColorWrapper,
    enabled: Boolean,
    onColorChanged: (ColorType, Float) -> Unit
) {
    TextFieldSliders(specifications = listOf(
        TextFieldSliderSpecification(
            enabled = enabled,
            errorMessages = null,
            keyboardType = KeyboardType.Number,
            textFieldLabel = stringResource(R.string.edit_art_style_color_red),
            sliderValue = color.red,
            textFieldValue = color.outOfBoundsRed?.times(255f)?.roundToInt()?.toString() ?: color.redAsEightBit.toString(),
            sliderRange = ColorWrapper.VALUE_RANGE,
            onSliderChanged = { onColorChanged(ColorType.RED, it) },
            onTextFieldChanged = { str ->
                str.toFloatOrNull()?.div(255f)?.let {
                    onColorChanged(ColorType.RED, it)
                }
            }
        ),
        TextFieldSliderSpecification(
            enabled = enabled,
            errorMessages = null,
            keyboardType = KeyboardType.Number,
            textFieldLabel = stringResource(R.string.edit_art_style_color_green),
            sliderValue = color.green,
            textFieldValue = color.outOfBoundsGreen?.times(255f)?.roundToInt()?.toString() ?: color.greenAsEightBit.toString(),
            sliderRange = ColorWrapper.VALUE_RANGE,
            onSliderChanged = { onColorChanged(ColorType.GREEN, it) },
            onTextFieldChanged = { str ->
                str.toFloatOrNull()?.div(255f)?.let {
                    onColorChanged(ColorType.GREEN, it)
                }
            }
        ),
        TextFieldSliderSpecification(
            enabled = enabled,
            errorMessages = null,
            keyboardType = KeyboardType.Number,
            textFieldLabel = stringResource(R.string.edit_art_style_color_blue),
            sliderValue = color.blue,
            textFieldValue = color.outOfBoundsBlue?.times(255f)?.roundToInt()?.toString() ?: color.blueAsEightBit.toString(),
            sliderRange = ColorWrapper.VALUE_RANGE,
            onSliderChanged = { onColorChanged(ColorType.BLUE, it) },
            onTextFieldChanged = { str ->
                str.toFloatOrNull()?.div(255f)?.let {
                    onColorChanged(ColorType.BLUE, it)
                }
            }
        )
    ))
}