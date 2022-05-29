package com.company.activityart.presentation.filter_distance_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.company.activityart.presentation.ui.theme.StravaOrange

@Composable
fun DistanceGraphComposable(
    distanceRange: IntRange,
    selectedMiles: IntRange,
    distancesHeightMap: Map<Int, Float>,
    maxHeight: Dp
) {
    Card(elevation = 4.dp) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(Color.Gray)
            )
        }
    }
}