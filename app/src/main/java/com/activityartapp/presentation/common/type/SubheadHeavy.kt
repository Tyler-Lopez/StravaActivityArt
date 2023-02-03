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
fun SubheadHeavy(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    textColor: Color? = Color.Black // TODO
) {
    Text(
        text = text,
        textAlign = textAlign,
        color = textColor ?: colorResource(id = R.color.light_text_secondary),
        style = MaterialTheme.typography.subtitle2,
        modifier = modifier
    )
}