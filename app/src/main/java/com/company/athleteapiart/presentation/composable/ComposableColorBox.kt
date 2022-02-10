package com.company.athleteapiart.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.company.athleteapiart.ui.spacing
import com.company.athleteapiart.ui.theme.StravaOrange

@Composable
fun ComposableColorBox(
    color: Color
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.sm)
            .height(64.dp)
            .background(
                color = Color(
                    color.red,
                    color.green,
                    color.blue
                )
            )
            .border(4.dp, StravaOrange)
            .padding(4.dp)
            .border(
                width = 5.dp,
                color = Color(1f, 1f, 1f, 0.2f)
            )
    )
}