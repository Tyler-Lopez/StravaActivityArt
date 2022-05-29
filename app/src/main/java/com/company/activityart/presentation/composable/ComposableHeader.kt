package com.company.activityart.presentation.composable

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.company.activityart.presentation.ui.theme.Asphalt
import com.company.activityart.presentation.ui.theme.MaisonNeue

@Composable
fun ComposableHeader(
    text: String,
    isBold: Boolean = false,
    color: Color = Asphalt,
    center: Boolean = false,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        textAlign = if (center) TextAlign.Center else TextAlign.Start,
        fontSize = 36.sp,
        fontFamily = MaisonNeue,
        color = color,
        fontWeight = FontWeight.Black,
        modifier = modifier
    )
}
