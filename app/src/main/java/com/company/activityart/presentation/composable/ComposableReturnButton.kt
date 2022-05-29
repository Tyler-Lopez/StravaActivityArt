package com.company.activityart.presentation.composable

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.company.activityart.presentation.ui.theme.StravaOrange
import com.company.activityart.presentation.ui.theme.White

@Composable
fun ComposableReturnButton(
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.clip(CircleShape),
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(backgroundColor = StravaOrange),
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)

        ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Return to the previous page",
            tint = White
        )
    }
}