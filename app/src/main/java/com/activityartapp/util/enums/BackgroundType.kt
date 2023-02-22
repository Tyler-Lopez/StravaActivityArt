package com.activityartapp.util.enums

import androidx.annotation.StringRes
import com.activityartapp.R

enum class BackgroundType(
    @StringRes val strRes: Int,
    @StringRes val backgroundColorStrRes: Pair<Int, Int>?
) {
    GRADIENT(
        strRes = R.string.edit_art_style_background_type_gradient,
        backgroundColorStrRes = R.string.edit_art_style_background_gradient_header
                to R.string.edit_art_style_background_gradient_description
    ),
    SOLID(
        strRes = R.string.edit_art_style_background_type_solid,
        backgroundColorStrRes = R.string.edit_art_style_background_solid_header
                to R.string.edit_art_style_background_solid_description
    ),
    TRANSPARENT(
        strRes = R.string.edit_art_style_background_type_transparent,
        backgroundColorStrRes = null
    );
}