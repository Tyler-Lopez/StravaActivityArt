package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import com.company.activityart.architecture.BaseChildViewModel
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.*
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewState.*
import com.company.activityart.util.TimeUtils
import com.company.activityart.util.YearMonthDay
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditArtFiltersViewModel @Inject constructor(
    getActivitiesFromCacheUseCase: GetActivitiesFromCacheUseCase,
    private val timeUtils: TimeUtils
) : BaseChildViewModel<
        EditArtFiltersViewState,
        EditArtFiltersViewEvent,
        EditArtViewEvent
        >() {

    private val activities = getActivitiesFromCacheUseCase().flatMap { it.value }
    private val activitiesUnixSeconds = activities.map {
        timeUtils.iso8601StringToUnixSecond(it.iso8601LocalDate)
    }
    private val unixSecondFirst: Float = activitiesUnixSeconds.min().toFloat()
    private val unixSecondLast: Float = activitiesUnixSeconds.max().toFloat()
    private val unixSecondRangeTotal = unixSecondFirst..unixSecondLast

    init {
        pushState(Loading)
        initFilters()
    }

    override fun onEvent(event: EditArtFiltersViewEvent) {
        when (event) {
            is DateRangeMonthsChanged -> onDateRangeMonthsChanged()
            is DateRangeYearsChanged -> onDateRangeYearsChanged()
        }
    }

    private fun onDateRangeMonthsChanged() {

    }

    private fun onDateRangeYearsChanged() {

    }

    private fun initFilters() {
        val tmpYMD = YearMonthDay(0, 0, 0)
        pushState(
            Standby(
                dateRangeSecondsSelected = unixSecondRangeTotal,
                dateRangeSecondsSelectedYMDStart = tmpYMD,
                dateRangeSecondsSelectedYMDEnd = tmpYMD,
                dateRangeSecondsTotal = unixSecondRangeTotal,
                dateRangeYearsSelected = unixSecondRangeTotal,
                dateRangeYearsTotal = unixSecondRangeTotal
            )
        )
    }

}