package com.company.athleteapiart.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.presentation.ui.theme.Lato

@Composable
fun ButtonComposable(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = { if (!enabled) onClick() },
        modifier = modifier
            .alpha(if (enabled) 0.5f else 1f)
    ) {
        Text(
            text = text,
            fontFamily = Lato,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}