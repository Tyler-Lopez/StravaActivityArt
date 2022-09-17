package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import com.company.activityart.architecture.ViewEvent
import com.company.activityart.architecture.ViewState
import java.time.YearMonth
import androidx.compose.material.RangeSlider
import com.company.activityart.util.classes.YearMonthDay

sealed interface EditArtFiltersViewEvent : ViewEvent {
    sealed interface DateChanged : EditArtFiltersViewEvent {
        val changedTo: YearMonthDay

        data class DateChangedAfter(
            override val changedTo: YearMonthDay
        ) : DateChanged
        data class DateChangedBefore(
            override val changedTo: YearMonthDay
        ) : DateChanged
    }
}

sealed interface EditArtFiltersViewState : ViewState {
    object Loading : EditArtFiltersViewState

    /**
     */
    data class Standby(
        val dateMaxDateUnixMilliseconds: Long,
        val dateMinDateUnixMilliSeconds: Long,
        val dateYearMonthDayAfter: YearMonthDay,
        val dateYearMonthDayBefore: YearMonthDay
    ) : EditArtFiltersViewState
}