package com.activityartapp.util.enums

import androidx.annotation.StringRes
import com.activityartapp.R

enum class BackgroundType(@StringRes val strRes: Int) {
    SOLID(strRes = R.string.edit_art_style_background_type_solid),
    GRADIENT(strRes = R.string.edit_art_style_background_type_gradient),
    TRANSPARENT(strRes = R.string.edit_art_style_background_type_transparent);
}