package com.company.athleteapiart.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.company.athleteapiart.presentation.ui.spacing
import com.company.athleteapiart.presentation.ui.theme.StravaOrange

@Composable
fun ComposableAppNameHorizontal(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ComposableHeader(
            text = "ACTIVITIES",
            isBold = true
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.sm)
                .width(4.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(StravaOrange)
        )
        ComposableHeader(
            text = "VISUALIZER",
            isBold = true
        )
    }
}