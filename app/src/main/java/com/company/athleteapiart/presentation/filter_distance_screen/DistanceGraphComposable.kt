package com.company.athleteapiart.presentation.filter_distance_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.company.athleteapiart.presentation.ui.theme.StravaOrange

@Composable
fun DistanceGraphComposable(
    distanceRange: IntRange,
    selectedMiles: IntRange,
    distancesHeightMap: Map<Int, Float>,
    maxHeight: Dp
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(156.dp, maxHeight * 0.75f)
            .drawBehind {

                val size = this.size

                val barWidth = size.width / (distanceRange.last - distanceRange.first)

                var i = 0f

                for (mile in distanceRange) {

                    val barHeight =
                        size.height * (distancesHeightMap[mile] ?: 0f)

                    val offset = Offset(
                        x = (barWidth * i) - (barWidth / 2f),
                        y = size.height - barHeight
                    )

                    drawRect(
                        color = if (mile in selectedMiles) StravaOrange else Gray,
                        topLeft = offset,
                        size = Size(
                            barWidth,
                            barHeight
                        )
                    )
                    i++
                }
            })
}