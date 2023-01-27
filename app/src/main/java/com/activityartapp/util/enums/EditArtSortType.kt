package com.activityartapp.util.enums

import androidx.annotation.StringRes
import com.activityartapp.R

enum class EditArtSortType(@StringRes val strRes: Int) {
    DATE(R.string.edit_art_sort_type_date),
    DISTANCE(R.string.edit_art_sort_type_distance),
    TYPE(R.string.edit_art_sort_type_type);
}