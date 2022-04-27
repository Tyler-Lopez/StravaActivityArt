package com.company.athleteapiart.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.company.athleteapiart.presentation.ui.spacing
import com.company.athleteapiart.presentation.ui.theme.WarmGrey90

@Composable
fun ComposableRGBSlider(
    text: String,
    color: androidx.compose.ui.graphics.Color,
    value: Float,
    modifier: Modifier = Modifier,
    onValueChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = MaterialTheme.spacing.sm)
    ) {
        ComposableParagraph(text = text, color = color, isBold = true)
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
            color = WarmGrey90,
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
    )
    Spacer(modifier = Modifier.height(20.dp))
}