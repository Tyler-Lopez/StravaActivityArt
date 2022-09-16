package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import java.time.YearMonth

sealed class EditArtFiltersViewEvent : ViewEvent {
    data class DateRangeYearsChanged(
        val newRange: ClosedFloatingPointRange<Float>,
        val changeComplete: Boolean
    ) : EditArtFiltersViewEvent()

    data class DateRangeMonthsChanged(
        val newRange: ClosedFloatingPointRange<Float>
    ) : EditArtFiltersViewEvent()
}

sealed class EditArtFiltersViewState : ViewState {
    object Loading : EditArtFiltersViewState()
    data class Standby(
        val dateSelectedEnd: YearMonth,
        val dateSelectedStart: YearMonth,
        val dateSecondsSelected: ClosedFloatingPointRange<Float>,
        val dateSecondsTotal: ClosedFloatingPointRange<Float>,
        val dateYearsSelectedCount: Int,
        val dateYearsSteps: Int,
    ) : EditArtFiltersViewState()
}