package com.company.activityart.presentation.common.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.company.activityart.presentation.ui.theme.Asphalt
import com.company.activityart.presentation.ui.theme.Silver
import com.company.activityart.presentation.ui.theme.StravaOrange
import com.company.activityart.presentation.ui.theme.White

@Composable
fun HighEmphasisButton(
    enabled: Boolean,
    size: ButtonSize,
    modifier: Modifier = Modifier,
    text: String? = null,
    onClick: () -> Unit,
    ) {
    SpandexButton(
        buttonStyle = ButtonStyle(
            backgroundColorEnabled = StravaOrange,
            backgroundColorDisabled = Silver,
            textColorEnabled = White,
            textColorDisabled = Asphalt,
            size = size,
            strokeColorEnabled = null,
            strokeColorDisabled = null
        ),
        enabled = enabled,
        modifier = modifier,
        onClick = onClick,
        text = text
    )
}