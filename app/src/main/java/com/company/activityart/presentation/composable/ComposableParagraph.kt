package com.company.activityart.presentation.composable

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.company.activityart.presentation.ui.theme.Lato
import com.company.activityart.presentation.ui.theme.WarmGrey90

@Composable
fun ComposableParagraph(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = WarmGrey90,
    isBold: Boolean = false
) {
    Text(
        text = text,
        fontSize = 28.sp,
        fontFamily = Lato,
        textAlign = TextAlign.Center,
        color = color,
        fontWeight = if (isBold) FontWeight.SemiBold else FontWeight.Normal,
        modifier = modifier
    )
}