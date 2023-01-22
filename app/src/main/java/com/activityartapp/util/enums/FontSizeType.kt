package com.activityartapp.util.enums

import androidx.annotation.StringRes
import com.activityartapp.R

enum class FontSizeType(@StringRes val strRes: Int) {
    XS(R.string.font_size_type_xs),
    SMALL(R.string.font_size_type_small),
    MEDIUM(R.string.font_size_type_medium),
    LARGE(R.string.font_size_type_large),
    XL(R.string.font_size_type_xl)
}