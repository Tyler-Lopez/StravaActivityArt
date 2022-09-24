package com.company.activityart.presentation.edit_art_screen.subscreens.style.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.edit_art_screen.ColorType
import com.company.activityart.presentation.edit_art_screen.ColorWrapper
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.ArtMutatingEvent.*
import com.company.activityart.presentation.edit_art_screen.StyleType
import com.company.activityart.presentation.ui.theme.spacing

// Todo, rework this, very sloppy atm
@Composable
fun ColorSliderRow(
    colorName: String,
    colorValue: Float,
    colorType: ColorType,
    styleType: StyleType,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = ("%.0f".format(colorValue * 255)).run {
                if (this == "0") "" else this
            },
            placeholder = { Text("0") },
            onValueChange = {
                val asNormalizedFloat = it.toFloatOrNull()?.div(255f)
                eventReceiver.onEvent(
                    StylesColorChanged(
                        styleType = styleType,
                        colorType = colorType,
                        changedTo = asNormalizedFloat?.coerceIn(ColorWrapper.VALUE_RANGE) ?: 0f
                    )
                )
            },
            leadingIcon = { SubheadHeavy(text = colorName) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.width(128.dp)
        )
        Slider(
            modifier = Modifier.weight(1f, true),
            value = colorValue,
            valueRange = ColorWrapper.VALUE_RANGE,
            onValueChange = {
                eventReceiver.onEvent(
                    StylesColorChanged(
                        styleType = styleType,
                        colorType = colorType,
                        changedTo = it
                    )
                )
            }
        )

    }
}