package com.activityartapp.presentation.common.type

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import com.activityartapp.R

@Composable
fun Subhead(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color? = null
) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        color = textColor ?: colorResource(id = R.color.light_text_primary),
        style = MaterialTheme.typography.subtitle1,
        modifier = modifier
    )
}