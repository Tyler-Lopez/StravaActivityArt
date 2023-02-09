package com.activityartapp.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.activityartapp.presentation.common.layout.ColumnSmallSpacing
import com.activityartapp.presentation.editArtScreen.ColorType
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.presentation.ui.theme.spacing

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ColorSlider(
    color: ColorWrapper,
    colorType: ColorType,
    enabled: Boolean,
    onColorChanged: (Pair<ColorType, Float>) -> Unit
) {
    val focusManager = LocalFocusManager.current



    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.small)
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val a = remember { mutableStateOf(5) }
        SideEffect {
            a.value = 5
        }
        // https://stackoverflow.com/questions/72886062/outlinedtextfield-doesnt-resize-to-a-minimumwidth-in-compose
        BasicTextField(
            value = color.textFieldValueFromType(colorType),
            onValueChange = { },
            interactionSource = interactionSource,
            enabled = enabled,
            singleLine = true,
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = color.textFieldValueFromType(colorType),
                visualTransformation = VisualTransformation.None,
                innerTextField = it,
                singleLine = true,
                enabled = enabled,
                label = {
                    Text(
                        text = stringResource(colorType.strRes),
                        style = MaterialTheme.typography.overline
                    )
                },
                interactionSource = interactionSource,
                // keep vertical paddings but change the horizontal
                contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                    start = 8.dp, end = 8.dp
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors()
            )
        }
        Slider(
            value = color.colorFromType(colorType),
            enabled = enabled,
            valueRange = ColorWrapper.VALUE_RANGE,
            modifier = Modifier.weight(1f, false),
            onValueChange = { onColorChanged(colorType to it) }
        )
    }
}

private fun ColorWrapper.textFieldValueFromType(colorType: ColorType): String {
    return when (colorType) {
        ColorType.RED -> outOfBoundsRed ?: redAsEightBit.toString()
        ColorType.GREEN -> outOfBoundsGreen ?: greenAsEightBit.toString()
        ColorType.BLUE -> outOfBoundsBlue ?: blueAsEightBit.toString()
        ColorType.ALPHA -> outOfBoundsAlpha ?: error("Not yet implemented") // todo
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

// https://stackoverflow.com/questions/70500071/measuring-string-width-to-properly-size-text-composable
@Composable
fun MeasureUnconstrainedViewWidth(
    viewToMeasure: @Composable () -> Unit,
    content: @Composable (measuredWidth: Dp) -> Unit,
) {
    SubcomposeLayout { constraints ->
        val measuredWidth = subcompose("viewToMeasure", viewToMeasure)[0]
            .measure(Constraints()).width.toDp()

        val contentPlaceable = subcompose("content") {
            content(measuredWidth)
        }[0].measure(constraints)
        layout(contentPlaceable.width, contentPlaceable.height) {
            contentPlaceable.place(0, 0)
        }
    }
}