package com.company.activityart.presentation.edit_art_screen.subscreens.style.composables

import androidx.compose.foundation.layout.*
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
fun ColorSlider(
    colorName: String,
    colorValue: Float,
    colorType: ColorType,
    styleType: StyleType,
    eventReceiver: EventReceiver<EditArtViewEvent>
) {
    Column(verticalArrangement = Arrangement.spacedBy(spacing.medium)) {
        SubheadHeavy(colorName, modifier = Modifier.padding(start = spacing.small))
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