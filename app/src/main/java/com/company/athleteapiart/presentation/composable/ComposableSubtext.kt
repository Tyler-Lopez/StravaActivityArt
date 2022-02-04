package com.company.athleteapiart.presentation.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.ui.theme.*

@Composable
fun ComposableSubtext(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = WarmGrey80,
) {
    Text(
        text = text,
        fontSize = 24.sp,
        fontFamily = Lato,
        fontWeight = FontWeight.Medium,
      //  fontStyle = FontStyle.Italic,
        textAlign = TextAlign.Start,
        color = color,
        modifier = modifier
    )
}