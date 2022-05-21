package com.company.athleteapiart.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.presentation.ui.theme.Lato

@Composable
fun HeaderComposable(
    text: String,
    textAlign: TextAlign = TextAlign.Center
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = text,
            textAlign = textAlign,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Lato,
            fontSize = 28.sp
        )
    }
}