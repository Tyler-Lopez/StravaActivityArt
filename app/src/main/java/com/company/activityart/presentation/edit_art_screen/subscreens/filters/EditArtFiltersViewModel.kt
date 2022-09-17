package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import com.company.activityart.architecture.BaseChildViewModel
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.*
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.DateChanged.*
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewState.*
import com.company.activityart.util.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
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

    /** ALL ACTIVITIES [Activity] **/
    private val activities = getActivitiesFromCacheUseCase().flatMap { it.value }

    /** DATE FILTERS **/
    /* All activities mapped to their unix seconds */
    private val activitiesUnixSeconds = activities.map {
        timeUtils.iso8601StringToUnixSecond(it.iso8601LocalDate)
    }

    init {
        pushState(Loading)
        initFilters()
    }

    override fun onEvent(event: EditArtFiltersViewEvent) {
        when (event) {
            is DateChanged -> onDateChanged(event)
        }
    }

    private fun onDateChanged(event: DateChanged) {
        (lastPushedState as? Standby)?.run {
            if (event is DateChangedAfter) {
                copy(dateYearMonthDayAfter = event.changedTo)
            } else {
                copy(dateYearMonthDayBefore = event.changedTo)
            }
        }?.push()
    }


    private fun initFilters() {
        timeUtils.apply {
            val unixMillisecondFirst = activitiesUnixSeconds.min()
            val unixMillisecondLast = activitiesUnixSeconds.max()

            val yearMonthStart = timeUtils.unixSecondToYearMonthDay(unixMillisecondFirst)
            val yearMonthEnd = timeUtils.unixSecondToYearMonthDay(unixMillisecondLast)

            pushState(
                Standby(
                    dateMaxDateUnixMilliseconds = TimeUnit.SECONDS.toMillis(unixMillisecondLast),
                    dateMinDateUnixMilliSeconds = TimeUnit.SECONDS.toMillis(unixMillisecondFirst),
                    dateYearMonthDayAfter = yearMonthStart,
                    dateYearMonthDayBefore = yearMonthEnd
                )
            )
        }
    }
}