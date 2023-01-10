package com.company.activityart.util.enums

import androidx.annotation.StringRes
import com.company.activityart.R

enum class FontWeightType(@StringRes val stringRes: Int) {
    BLACK(R.string.font_weight_type_black),
    BOLD(R.string.font_weight_type_bold),
    SEMIBOLD(R.string.font_weight_type_semibold),
    MEDIUM(R.string.font_weight_type_medium),
    REGULAR(R.string.font_weight_type_regular),
    LIGHT(R.string.font_weight_type_light),
    EXTRA_LIGHT(R.string.font_weight_type_extra_light),
    THIN(R.string.font_weight_type_thin);
}