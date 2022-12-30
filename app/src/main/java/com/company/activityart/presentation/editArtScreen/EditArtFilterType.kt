package com.company.activityart.presentation.editArtScreen

/**
 * Ordinal position
 */
enum class EditArtFilterType {
    DATE, TYPE, DISTANCE;

    inline fun forEachNextFilterType(action: (EditArtFilterType) -> Unit) {
        values().onEach { if (it.ordinal > ordinal) action(it) }
    }

    val lastFilter: EditArtFilterType?
        get() { return if (ordinal == 0) null else values[ordinal - 1] }

    companion object {
        private val values = values()
        val filterFinal = values.last()
    }
}