package com.company.athleteapiart.presentation.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.ui.theme.Lato
import com.company.athleteapiart.ui.theme.Roboto
import com.company.athleteapiart.ui.theme.WarmGrey50
import com.company.athleteapiart.ui.theme.WarmGrey90

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