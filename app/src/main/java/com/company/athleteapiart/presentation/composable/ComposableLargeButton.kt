package com.company.athleteapiart.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.company.athleteapiart.ui.spacing
import com.company.athleteapiart.ui.theme.StravaOrange
import com.company.athleteapiart.ui.theme.WarmGrey20
import com.company.athleteapiart.ui.theme.White

@Composable
fun ComposableLargeButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val isThin = this.maxWidth < 414.dp
        Button(
            colors = ButtonDefaults.buttonColors(StravaOrange),
            onClick = {
                onClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = if (isThin) 0.dp else MaterialTheme.spacing.md,
                    vertical = if (isThin) 0.dp else MaterialTheme.spacing.sm
                ),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomStart = if (isThin) 0.dp else 5.dp,
                bottomEnd = if (isThin) 0.dp else 5.dp
            )
        ) {
            ComposableHeader(
                text = text,
                color = White,
                isBold = true
            )
        }
    }
}