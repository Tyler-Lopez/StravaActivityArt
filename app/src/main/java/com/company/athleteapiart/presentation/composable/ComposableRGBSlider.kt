package com.company.athleteapiart.presentation.composable

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.company.athleteapiart.ui.theme.StravaOrange
import com.company.athleteapiart.ui.theme.WarmGrey40
import com.company.athleteapiart.ui.theme.WarmGrey50
import com.company.athleteapiart.util.meterToMiles

@Composable
fun ComposableRGBSlider(
    text: String,
    color: androidx.compose.ui.graphics.Color,
    value: Float,
    onValueChange: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        ComposableParagraph(text = text, color = color)
        Spacer(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .width(2.dp)
                .height(25.dp)
                .clip(RoundedCornerShape(2.dp))
            // .background(WarmGrey50)
        )
        ComposableParagraph(
            text = "${value.toInt()}",
            color = WarmGrey50,
        )
    }
    Slider(
        value = value,
        onValueChange = {
            onValueChange(it.toInt())
        },
        colors = SliderDefaults.colors(activeTrackColor = color, thumbColor = color),
        valueRange = 0f..255f,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .drawBehind {
                val incrementWidth =
                    (this.size.width - 18.dp.toPx()) / 4f
                for (i in 0..4) {
                    drawLine(
                        color = color,
                        start = Offset(
                            x = 9.dp.toPx() + i * incrementWidth,
                            y = this.center.y - 10f
                        ),
                        end = Offset(
                            x = 9.dp.toPx() + i * incrementWidth,
                            y = this.center.y + 10f
                        ),
                        cap = StrokeCap.Round,
                        strokeWidth = 3f
                    )
                }
            }
    )
    Spacer(modifier = Modifier.height(20.dp))
}