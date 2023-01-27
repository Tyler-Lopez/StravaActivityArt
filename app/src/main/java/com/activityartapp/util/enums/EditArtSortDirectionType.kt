package com.activityartapp.util.enums

import androidx.annotation.StringRes
import com.activityartapp.R

enum class EditArtSortDirectionType(@StringRes val headerStrRes: Int) {
    ASCENDING(R.string.edit_art_sort_direction_ascending_header) {
        @StringRes
        override fun description(sortType: EditArtSortType): Int {
            return when (sortType) {
                EditArtSortType.DATE -> R.string.edit_art_sort_direction_ascending_description_date
                EditArtSortType.DISTANCE -> R.string.edit_art_sort_direction_ascending_description_distance
                EditArtSortType.TYPE -> R.string.edit_art_sort_direction_ascending_description_type
            }
        }
    },
    DESCENDING(R.string.edit_art_sort_direction_descending_header) {
        @StringRes
        override fun description(sortType: EditArtSortType): Int {
            return when (sortType) {
                EditArtSortType.DATE -> R.string.edit_art_sort_direction_descending_description_date
                EditArtSortType.DISTANCE -> R.string.edit_art_sort_direction_descending_description_distance
                EditArtSortType.TYPE -> R.string.edit_art_sort_direction_descending_description_type
            }
        }
    };

    @StringRes
    abstract fun description(sortType: EditArtSortType): Int
}