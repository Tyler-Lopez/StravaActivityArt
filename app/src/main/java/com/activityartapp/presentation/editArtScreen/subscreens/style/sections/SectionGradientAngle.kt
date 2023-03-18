package com.activityartapp.presentation.editArtScreen.subscreens.style.sections

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.activityartapp.presentation.editArtScreen.ColorWrapper
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleGradientAngleTypeChanged
import com.activityartapp.util.GradientAngleUtils
import com.activityartapp.util.enums.AngleType
import kotlin.math.roundToInt

@Composable
fun SectionGradientAngle(
    angleType: AngleType,
    colorList: List<ColorWrapper>,
    onGradientAngleTypeChanged: (StyleGradientAngleTypeChanged) -> Unit
) {
    val values = AngleType.values()
    val utils = GradientAngleUtils()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Card(modifier = Modifier.size(96.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val offsets = utils.getStartAndEndOffsets(
                    angleType = angleType,
                    size = Size(this.size.width, this.size.height)
                )
                drawRect(
                    brush = Brush.linearGradient(
                        colors = colorList.map { it.toColor() },
                        start = offsets.first,
                        end = offsets.second
                    )
                )
            }
        }
    }
    Slider(
        value = angleType.ordinal.toFloat(),
        onValueChange = {
            val ordinal = it.roundToInt()
            onGradientAngleTypeChanged(
                StyleGradientAngleTypeChanged(
                    changedTo = AngleType.values()[ordinal]
                )
            )
        },
        valueRange = 0f..values.lastIndex.toFloat(),
        steps = values.size - 2
    )
}