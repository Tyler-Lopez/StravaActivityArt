package com.company.activityart.presentation.editArtScreen

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.company.activityart.R

enum class EditArtHeaderType(
    @StringRes val textStr: Int,
    @StringRes val contentDescription: Int,
    val icon: ImageVector
) {
    PREVIEW(
        R.string.button_preview_uppercase,
        R.string.button_preview_cd,
        Icons.Default.Preview
    ),
    FILTERS(
        R.string.button_filters_uppercase,
        R.string.button_filters_cd,
        Icons.Default.FilterAlt
    ),
    STYLE(
        R.string.buttons_style_uppercase,
        R.string.buttons_style_cd,
        Icons.Default.Palette
    ),
    TYPE(
        R.string.buttons_type_uppercase,
        R.string.buttons_type_cd,
        Icons.Default.TextFields
    ),
    RESIZE(
        R.string.buttons_resize_uppercase,
        R.string.buttons_style_cd,
        Icons.Default.AspectRatio
    );

    companion object {
        fun fromOrdinal(ordinal: Int): EditArtHeaderType? {
            return values().firstOrNull { it.ordinal == ordinal }
        }
    }
}