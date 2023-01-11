package com.activityartapp.presentation.editArtScreen.subscreens.type

import androidx.annotation.StringRes
import com.activityartapp.R

enum class EditArtTypeSection(
    @StringRes val header: Int,
    @StringRes val description: Int
) {
    LEFT(
        R.string.edit_art_type_section_left_header,
        R.string.edit_art_type_section_left_description
    ),
    CENTER(
        R.string.edit_art_type_section_center_header,
        R.string.edit_art_type_section_center_description
    ),
    RIGHT(
        R.string.edit_art_type_section_right_header,
        R.string.edit_art_type_section_right_description
    );
}