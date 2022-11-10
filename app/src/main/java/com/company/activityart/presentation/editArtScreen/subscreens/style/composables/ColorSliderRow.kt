package com.company.activityart.presentation.editArtScreen.subscreens.style.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.type.SubheadHeavy
import com.company.activityart.presentation.editArtScreen.ColorType
import com.company.activityart.presentation.editArtScreen.ColorWrapper
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.*
import com.company.activityart.presentation.editArtScreen.StyleType
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