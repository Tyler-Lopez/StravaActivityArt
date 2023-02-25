package com.activityartapp.util.enums

import androidx.annotation.StringRes
import com.activityartapp.R

enum class BackgroundType(@StringRes val strRes: Int) {
    SOLID(strRes = R.string.edit_art_style_background_type_solid),
    GRADIENT_LINEAR(strRes = R.string.edit_art_style_background_type_linear_gradient),
    GRADIENT_RADIAL(strRes = R.string.edit_art_style_background_type_radial_gradient),
    TRANSPARENT(strRes = R.string.edit_art_style_background_type_transparent);
}