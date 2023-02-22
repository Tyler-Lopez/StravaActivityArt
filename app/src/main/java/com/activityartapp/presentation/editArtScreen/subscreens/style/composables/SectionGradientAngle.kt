package com.activityartapp.presentation.editArtScreen.subscreens.style.composables

import android.util.Size
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent
import com.activityartapp.presentation.editArtScreen.composables.Section
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.StyleGradientAngleTypeChanged
import com.activityartapp.util.GradientAngleUtils
import com.activityartapp.util.enums.AngleType
import kotlin.math.roundToInt

@Composable
fun SectionGradientAngle(
    angleType: AngleType,
    onGradientAngleTypeChanged: (StyleGradientAngleTypeChanged) -> Unit
) {
    Section(
        header = "Gradient Angle",
        description = "The gradient angle specifies the direction in which the color gradient changes."
    ) {
        val values = AngleType.values()
        val utils = GradientAngleUtils()
        Canvas(modifier = Modifier.size(254.dp)) {
            val offsets = utils.getStartAndEndOffsets(
                angleType,
                Size(this.size.width.toInt(), this.size.height.toInt())
            )
            drawCircle(
                brush = Brush.linearGradient(colors = listOf(Color.Black, Color.Gray)),

            )
        }
        Slider(
            value = angleType.ordinal.toFloat(),
            onValueChange = {
                val ordinal = it.roundToInt()
                println("ordinal changed from $angleType to $ordinal")
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
}