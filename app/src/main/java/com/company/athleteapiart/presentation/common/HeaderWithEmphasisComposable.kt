package com.company.athleteapiart.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.presentation.ui.theme.Lato
import com.company.athleteapiart.presentation.ui.theme.StravaOrange

@Composable
fun HeaderWithEmphasisComposable(
    emphasized: String,
    textAlign: TextAlign = TextAlign.Center
) {
    val headerText = "Which %s would you like to include?".format(emphasized)
    val start = headerText.indexOf(emphasized)

    val spanStyles = listOf(
        AnnotatedString.Range(
            SpanStyle(
                fontWeight = FontWeight.Bold,
                color = StravaOrange
            ),
            start = start,
            end = start + emphasized.length
        )
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = AnnotatedString(
                text = headerText,
                spanStyles = spanStyles,
            ),
            textAlign = textAlign,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Lato,
            fontSize = 28.sp
        )
    }
}