package com.company.athleteapiart.presentation.composable

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.ui.theme.Lato
import com.company.athleteapiart.ui.theme.WarmGrey50

@Composable
fun ComposableHeader(
    text: String,
    isBold: Boolean = false,
    color: Color = WarmGrey50,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        fontSize = 35.sp,
        fontFamily = Lato,
        letterSpacing = 1.sp,
        color = color,
        fontWeight = if (isBold) FontWeight.SemiBold else FontWeight.Normal,
        modifier = modifier
    )
}
