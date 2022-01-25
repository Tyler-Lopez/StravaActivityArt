package com.company.athleteapiart.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.company.athleteapiart.presentation.destinations.ActivitiesScreenDestination
import com.company.athleteapiart.ui.theme.White
import com.company.athleteapiart.util.AthleteActivities

@Composable
fun ComposableLargeButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            onClick = {
                onClick()
            }
        ) {
            ComposableHeader(
                text = text,
                color = White
            )
        }
    }
}