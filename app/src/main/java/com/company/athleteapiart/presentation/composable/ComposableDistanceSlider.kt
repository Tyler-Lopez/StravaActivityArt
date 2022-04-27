package com.company.athleteapiart.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.company.athleteapiart.presentation.ui.spacing
import com.company.athleteapiart.presentation.ui.theme.StravaOrange
import com.company.athleteapiart.presentation.ui.theme.WarmGrey90
import com.company.athleteapiart.util.meterToMiles

@Composable
fun ComposableDistanceSlider(
    header: String,
    value: Float,
    color: androidx.compose.ui.graphics.Color = WarmGrey90,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = MaterialTheme.spacing.sm)
    ) {
        ComposableParagraph(
            text = header,
            color = color
        )
        Spacer(modifier = Modifier
            .padding(horizontal = 5.dp)
            .width(2.dp)
            .height(25.dp)
            .clip(RoundedCornerShape(2.dp))
           // .background(WarmGrey50)
        )
        ComposableParagraph(
            text = "%.1f"
                .format(
                    (value)
                        .toDouble()
                        .meterToMiles()
                ) + " mi",
            color = StravaOrange,
        )
    }
    Slider(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        valueRange = valueRange,
        modifier = Modifier.padding(horizontal = 10.dp)
    )
}