package com.company.activityart.presentation.common.button

import androidx.compose.ui.graphics.Color

data class ButtonStyle(
    val backgroundColorEnabled: Color,
    val backgroundColorDisabled: Color,
    val textColorEnabled: Color,
    val textColorDisabled: Color,
    val size: ButtonSize,
    val strokeColorEnabled: Color? = null,
    val strokeColorDisabled: Color? = null
)
