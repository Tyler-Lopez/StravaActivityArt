package com.activityartapp.presentation.common.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.activityartapp.R

enum class ButtonSize {
    LARGE, MEDIUM, SMALL, EXTRA_SMALL;

    @Composable
    fun getMinHeight(): Dp {
        return dimensionResource(
            when (this) {
                LARGE -> R.dimen.button_height_large
                MEDIUM -> R.dimen.button_height_medium
                SMALL -> R.dimen.button_height_small
                EXTRA_SMALL -> R.dimen.button_height_extra_small
            }
        )
    }
}