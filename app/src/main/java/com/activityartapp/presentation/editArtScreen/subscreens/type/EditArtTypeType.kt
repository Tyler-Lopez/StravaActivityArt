package com.activityartapp.presentation.editArtScreen.subscreens.type

import androidx.annotation.StringRes
import com.activityartapp.R

enum class EditArtTypeType(@StringRes val header: Int) {
    NONE(R.string.edit_art_type_type_none),
   // NAME(R.string.edit_art_type_type_name),
    DISTANCE_MILES(R.string.edit_art_type_type_distance_miles),
    DISTANCE_KILOMETERS(R.string.edit_art_type_type_distance_meters),
    CUSTOM(R.string.edit_art_type_type_custom);
}