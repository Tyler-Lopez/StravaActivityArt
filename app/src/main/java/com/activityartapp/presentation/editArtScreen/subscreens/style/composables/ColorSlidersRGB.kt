package com.activityartapp.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.activityartapp.R
import com.activityartapp.presentation.editArtScreen.ColorType
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.presentation.editArtScreen.composables.TextFieldSliderSpecification
import com.activityartapp.presentation.editArtScreen.composables.TextFieldSliders

@Composable
fun ColorSlidersRGB(
    color: ColorWrapper,
    onColorChanged: (ColorType, Float) -> Unit,
    onColorPendingChanged: (ColorType, String) -> Unit,
    onColorPendingChangeConfirmed: (ColorType) -> Unit
) {
    TextFieldSliders(
        specifications = ColorType.values().filter { it != ColorType.ALPHA }.map { colorType ->
            TextFieldSliderSpecification(
                keyboardType = KeyboardType.Number,
                textFieldLabel = stringResource(colorType.strRes),
                pendingChangesMessage = when (colorType) {
                    ColorType.RED -> color.pendingRed
                    ColorType.GREEN -> color.pendingGreen
                    ColorType.BLUE -> color.pendingBlue
                    ColorType.ALPHA -> color.pendingAlpha
                }?.let {
                    stringResource(R.string.edit_art_style_color_pending_change_prompt)
                },
                sliderValue = when (colorType) {
                    ColorType.RED -> color.red
                    ColorType.GREEN -> color.green
                    ColorType.BLUE -> color.blue
                    else -> color.alpha
                },
                textFieldValue = when (colorType) {
                    ColorType.RED -> color.pendingRed ?: color.redToEightBitString()
                    ColorType.GREEN -> color.pendingGreen ?: color.greenToEightBitString()
                    ColorType.BLUE -> color.pendingBlue ?: color.blueToEightBitString()
                    else -> color.pendingAlpha ?: color.alphaToEightBitString()
                },
                sliderRange = ColorWrapper.RATIO_RANGE,
                onSliderChanged = { onColorChanged(colorType, it) },
                onTextFieldChanged = { onColorPendingChanged(colorType, it) },
                onTextFieldDone = { onColorPendingChangeConfirmed(colorType) }
            )
        }
    )
}