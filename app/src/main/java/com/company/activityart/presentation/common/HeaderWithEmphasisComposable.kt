package com.company.activityart.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

@Composable
fun HeaderWithEmphasisComposable(
    emphasized: String,
    string: String = "Which %s would you like to include?",
    textAlign: TextAlign = TextAlign.Center
) {
    /*
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

     */
}