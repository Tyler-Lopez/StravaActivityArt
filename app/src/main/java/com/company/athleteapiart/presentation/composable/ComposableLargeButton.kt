package com.company.athleteapiart.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.company.athleteapiart.ui.spacing
import com.company.athleteapiart.ui.theme.White

@Composable
fun ComposableLargeButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(horizontal = MaterialTheme.spacing.md),
        contentAlignment = Alignment.Center
    ) {
        Button(
            modifier = Modifier
                .fillMaxSize(),
            onClick = {
                onClick()
            }
        ) {
            ComposableHeader(
                text = text,
                color = White,
                isBold = true
            )
        }
    }
}