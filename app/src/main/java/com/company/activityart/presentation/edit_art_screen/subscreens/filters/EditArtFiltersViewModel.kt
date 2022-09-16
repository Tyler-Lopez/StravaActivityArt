package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import com.company.activityart.architecture.BaseChildViewModel
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.*
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewState.*
import com.company.activityart.util.TimeUtils
import com.company.activityart.util.ext.closestValue
import com.company.activityart.util.ext.toFloatRange
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

    companion object {
        private const val START_AND_END_STEPS = 2
        private const val NO_STEPS = 0
    }

    /** ALL ACTIVITIES [Activity] **/
    private val activities = getActivitiesFromCacheUseCase().flatMap { it.value }

    /** DATE FILTERS **/
    /* All activities mapped to their unix seconds */
    private val activitiesUnixSeconds = activities.map {
        timeUtils.iso8601StringToUnixSecond(it.iso8601LocalDate)
    }
    /* First and last unix seconds of all activities */
    private val unixSecondFirst = activitiesUnixSeconds.min()
    private val unixSecondLast = activitiesUnixSeconds.max()
    /* Utilities around unix seconds */
    private val unixSecondsTotal = (unixSecondFirst..unixSecondLast).toFloatRange()
    private val unixSecondsTotalRange = unixSecondLast - unixSecondFirst
    private val yearMonthStart = timeUtils.unixSecondToYearMonth(unixSecondFirst)
    private val yearMonthEnd = timeUtils.unixSecondToYearMonth(unixSecondLast)
    private val yearsTotalRange = yearMonthEnd.year - yearMonthStart.year
    private val unixSecondsAtSteps = (0..yearsTotalRange).map {
        unixSecondFirst + ((unixSecondsTotalRange / yearsTotalRange) * it)
    }

    init {
        pushState(Loading)
        initFilters()
    }

    override fun onEvent(event: EditArtFiltersViewEvent) {
        when (event) {
            is DateRangeMonthsChanged -> onDateRangeMonthsChanged()
            is DateRangeYearsChanged -> onDateRangeYearsChanged(event)
        }
    }

    private fun onDateRangeMonthsChanged() {

    }

    private fun onDateRangeYearsChanged(event: DateRangeYearsChanged) {
        (lastPushedState as? Standby)?.run {
            event.run {
                if (changeComplete) {
                    /* Year date range will snap to the closest whole integer value */
                    val snapToStart = unixSecondsAtSteps.closestValue(newRange.start.toLong())
                    val snapToEnd = unixSecondsAtSteps.closestValue(newRange.endInclusive.toLong())
                    val yearMonthStart = timeUtils.unixSecondToYearMonth(snapToStart)
                    val yearMonthEnd = timeUtils.unixSecondToYearMonth(snapToEnd)
                    copy(
                        dateSelectedStart = yearMonthStart,
                        dateSelectedEnd = yearMonthEnd,
                        dateYearsSelectedCount = yearMonthEnd.year - yearMonthStart.year + 1
                    )
                } else {
                    copy(dateSecondsSelected = newRange)
                }
            }
        }?.push()
    }


    private fun initFilters() {
        timeUtils.apply {
            pushState(
                Standby(
                    dateSelectedStart = yearMonthStart,
                    dateSelectedEnd = yearMonthEnd,
                    dateSecondsSelected = unixSecondsTotal,
                    dateSecondsTotal = unixSecondsTotal,
                    dateYearsSelectedCount = yearsTotalRange + 1,
                    dateYearsSteps = (yearsTotalRange - 1).coerceAtLeast(NO_STEPS)
                )
            )
        }
    }
}