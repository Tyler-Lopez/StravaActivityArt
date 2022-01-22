package com.company.athleteapiart.presentation.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.ui.theme.Roboto
import com.company.athleteapiart.ui.theme.WarmGrey50

@Composable
fun ComposableParagraph(
    text: String,
    color: Color = WarmGrey50,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 24.sp,
        fontFamily = Roboto,
        textAlign = TextAlign.Center,
        color = color,
        modifier = modifier
    )
}