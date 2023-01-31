package com.activityartapp.util.enums

import androidx.annotation.StringRes
import com.activityartapp.R

enum class BackgroundType(@StringRes val strRes: Int) {
    SOLID(R.string.edit_art_style_background_type_solid),
    TRANSPARENT(R.string.edit_art_style_background_type_transparent)
}