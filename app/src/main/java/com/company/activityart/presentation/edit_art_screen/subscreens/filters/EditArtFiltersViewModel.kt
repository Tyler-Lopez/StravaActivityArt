package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseChildViewModel
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.*
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.DateChanged.*
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewState.*
import com.company.activityart.util.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    init {
        pushState(Loading)
        viewModelScope.launch(Dispatchers.Default) {
            initFilters()
        }
    }

    override fun onEvent(event: EditArtFiltersViewEvent) {
        viewModelScope.launch {
            when (event) {
                is DateChanged -> onDateChanged(event)
                is TypeToggleFlipped -> onTypeToggleFlipped(event)
            }
        }
    }

    private fun onDateChanged(event: DateChanged) {
        (lastPushedState as? Standby)?.run {
            val newAfter = if (event is DateChangedAfter) event.changedTo else dateYearMonthDayAfter
            val newBefore = if (event is DateChangedBefore) event.changedTo else dateYearMonthDayBefore
            onParentEvent(EditArtViewEvent.FilterDateChanged(
                // todo improve this
                newAfter.unixMilliseconds / 1000f,
                newBefore.unixMilliseconds / 1000f
            ))
            copy(
                dateYearMonthDayBefore = newBefore,
                dateYearMonthDayAfter = newAfter
            )
        }?.push()
    }

    private fun onTypeToggleFlipped(event: TypeToggleFlipped) {
        (lastPushedState as? Standby)?.run {
            copy(
                typesWithSelectedFlag = typesWithSelectedFlag.toMutableMap().also {
                    it[event.type] = !(it[event.type] ?: false)
                }
            )
        }?.push()
    }


    private suspend fun initFilters() {
        timeUtils.apply {
            /** Date filters **/
            val activitiesUnixSeconds =
                activities.map { iso8601StringToUnixSecond(it.iso8601LocalDate) }
            val unixMillisecondFirst = activitiesUnixSeconds.min()
            val unixMillisecondLast = activitiesUnixSeconds.max()
            val yearMonthStart = timeUtils.unixSecondToYearMonthDay(unixMillisecondFirst)
            val yearMonthEnd = timeUtils.unixSecondToYearMonthDay(unixMillisecondLast)

            /** Type filters **/
            val activityTypesWithSelectedFlag =
                activities.map { it.type }.distinct().associateWith { false }

            pushState(
                Standby(
                    dateMaxDateUnixMilliseconds = TimeUnit.SECONDS.toMillis(unixMillisecondLast),
                    dateMinDateUnixMilliSeconds = TimeUnit.SECONDS.toMillis(unixMillisecondFirst),
                    dateYearMonthDayAfter = yearMonthStart,
                    dateYearMonthDayBefore = yearMonthEnd,
                    typesWithSelectedFlag = activityTypesWithSelectedFlag
                )
            )
        }
    }
}