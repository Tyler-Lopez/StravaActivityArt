package com.activityartapp.presentation.common.type

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.activityartapp.R
import com.activityartapp.presentation.ui.theme.LightTextSecondary

@Composable
fun SubheadHeavy(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color? = LightTextSecondary
) {
    Text(
        text = text,
        color = textColor ?: colorResource(id = R.color.light_text_secondary),
        style = MaterialTheme.typography.subtitle2,
        modifier = modifier
    )
}