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
            errorMessage = color.outOfBoundsRed?.let {
                stringResource(
                    id = R.string.edit_art_style_color_too_large_error,
                    ColorWrapper.EIGHT_BIT_RANGE.last
                )
            },
            keyboardType = KeyboardType.Number,
            textFieldLabel = stringResource(R.string.edit_art_style_color_red),
            sliderValue = color.red,
            textFieldValue = color
                .outOfBoundsRed
                ?.let { ColorWrapper.ratioToEightBit(it).toString() }
                ?: color.redAsEightBit.toString(),
            sliderRange = ColorWrapper.RATIO_RANGE,
            onSliderChanged = { onColorChanged(ColorType.RED, it) },
            onTextFieldChanged = { str ->
                str.toIntOrNull()?.let {
                    onColorChanged(ColorType.RED, ColorWrapper.eightBitToRatio(it))
                }
            }
        ),
        TextFieldSliderSpecification(
            enabled = enabled,
            errorMessage = color.outOfBoundsGreen?.let {
                stringResource(
                    id = R.string.edit_art_style_color_too_large_error,
                    ColorWrapper.EIGHT_BIT_RANGE.last
                )
            },
            keyboardType = KeyboardType.Number,
            textFieldLabel = stringResource(R.string.edit_art_style_color_green),
            sliderValue = color.green,
            textFieldValue = color
                .outOfBoundsGreen
                ?.let { ColorWrapper.ratioToEightBit(it).toString() }
                ?: color.greenAsEightBit.toString(),
            sliderRange = ColorWrapper.RATIO_RANGE,
            onSliderChanged = { onColorChanged(ColorType.GREEN, it) },
            onTextFieldChanged = { str ->
                str.toIntOrNull()?.let {
                    onColorChanged(ColorType.GREEN, ColorWrapper.eightBitToRatio(it))
                }
            }
        ),
        TextFieldSliderSpecification(
            enabled = enabled,
            errorMessage = color.outOfBoundsBlue?.let {
                stringResource(
                    id = R.string.edit_art_style_color_too_large_error,
                    ColorWrapper.EIGHT_BIT_RANGE.last
                )
            },
            keyboardType = KeyboardType.Number,
            textFieldLabel = stringResource(R.string.edit_art_style_color_blue),
            sliderValue = color.blue,
            textFieldValue = color
                .outOfBoundsBlue
                ?.let { ColorWrapper.ratioToEightBit(it).toString() }
                ?: color.blueAsEightBit.toString(),
            sliderRange = ColorWrapper.RATIO_RANGE,
            onSliderChanged = { onColorChanged(ColorType.BLUE, it) },
            onTextFieldChanged = { str ->
                str.toIntOrNull()?.let {
                    onColorChanged(ColorType.BLUE, ColorWrapper.eightBitToRatio(it))
                }
            }
        )
    ))
}