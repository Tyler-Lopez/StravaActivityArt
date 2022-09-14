package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.FilterStateWrapper
import com.company.activityart.util.YearMonthDay

sealed class EditArtFiltersViewEvent : ViewEvent {
    data class DateRangeYearsChanged(
        val newRange: ClosedFloatingPointRange<Float>
    ) : EditArtFiltersViewEvent()
    data class DateRangeMonthsChanged(
        val newRange: ClosedFloatingPointRange<Float>
    ) : EditArtFiltersViewEvent()
}

sealed class EditArtFiltersViewState : ViewState {
    object Loading : EditArtFiltersViewState()
    data class Standby(
        val dateRangeSecondsSelected: ClosedFloatingPointRange<Float>,
        val dateRangeSecondsSelectedYMDStart: YearMonthDay,
        val dateRangeSecondsSelectedYMDEnd: YearMonthDay,
        val dateRangeSecondsTotal: ClosedFloatingPointRange<Float>,
        val dateRangeYearsSelected: ClosedFloatingPointRange<Float>,
        val dateRangeYearsTotal: ClosedFloatingPointRange<Float>,
    ) : EditArtFiltersViewState()
}