package com.company.activityart.presentation.editArtScreen

/**
 * Ordinal position 
 */
enum class EditArtFilterType {
    DATE, TYPE;

    inline fun forEachFollowingFilterType(action: (EditArtFilterType) -> Unit) {
        values().onEach { if (it.ordinal > ordinal) action(it) }
    }

    val lastFilter: EditArtFilterType?
        get() { return values[ordinal - 1].takeIf { it.ordinal > 0 } }

    val nextFilter: EditArtFilterType?
        get() { return values[ordinal + 1].takeIf { it.ordinal <= values.lastIndex } }

    companion object {
        private val values = EditArtFilterType.values()
    }
}