package com.company.athleteapiart.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.presentation.ui.theme.Lato
import com.company.athleteapiart.presentation.ui.theme.MaisonNeue
import com.company.athleteapiart.presentation.ui.theme.StravaOrange

@Composable
fun HeaderWithEmphasisComposable(
    emphasized: String,
    string: String = "Which %s would you like to include?",
    textAlign: TextAlign = TextAlign.Center
) {
    val headerText = string.format(emphasized)
    val start = headerText.indexOf(emphasized)

    val spanStyles = listOf(
        AnnotatedString.Range(
            SpanStyle(
                fontFamily = MaisonNeue,
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