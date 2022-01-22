package com.company.athleteapiart.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.company.athleteapiart.ui.theme.StravaOrange

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
            text = "ACTIVITIES"
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .width(2.dp)
                .height(30.dp)
                .background(StravaOrange)
        )
        ComposableHeader(
            text = "VISUALIZER",
            isBold = true
        )
    }
}