package com.company.athleteapiart.presentation.activity_select_screen.composable

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.company.athleteapiart.ui.theme.StravaOrange
import com.company.athleteapiart.ui.theme.White

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