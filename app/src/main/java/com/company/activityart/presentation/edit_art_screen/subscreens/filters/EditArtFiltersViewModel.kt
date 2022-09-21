package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseChildViewModel
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.FilterTypeChanged.*
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.FilterDateChanged
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.DateChanged
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.DateChanged.DateChangedAfter
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.DateChanged.DateChangedBefore
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.TypeToggleFlipped
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewState.Loading
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewState.Standby
import com.company.activityart.util.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.SECONDS
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
    private val activities by lazy {
        getActivitiesFromCacheUseCase().flatMap { it.value }
    }

    init {
        pushState(Loading)
        initFilters()
    }

    override fun onEvent(event: EditArtFiltersViewEvent) {
        viewModelScope.launch(Dispatchers.Default) {
            when (event) {
                is DateChanged -> onDateChanged(event)
                is TypeToggleFlipped -> onTypeToggleFlipped(event)
            }
        }
    }

    private fun onDateChanged(event: DateChanged) {
        (lastPushedState as? Standby)?.run {
            val newAfter = if (event is DateChangedAfter) {
                event.changedTo
            } else {
                dateYearMonthDayAfter
            }
            val newBefore = if (event is DateChangedBefore) {
                event.changedTo
            } else {
                dateYearMonthDayBefore
            }
            onParentEvent(
                FilterDateChanged(
                    MILLISECONDS.toSeconds(newAfter.unixMilliseconds),
                    MILLISECONDS.toSeconds(newBefore.unixMilliseconds)
                )
            )
            copy(
                dateYearMonthDayBefore = newBefore,
                dateYearMonthDayAfter = newAfter
            )
        }?.push()
    }

    private fun onTypeToggleFlipped(event: TypeToggleFlipped) {
        (lastPushedState as? Standby)?.run {
            val selectedMap = typesWithSelectedFlag.toMutableMap()
            val wasSelected = selectedMap[event.type] ?: error("Type missing")
            onParentEvent(
                if (wasSelected) {
                    FilterTypeRemoved(event.type)
                } else {
                    FilterTypeAdded(event.type)

                }
            )
            copy(
                typesWithSelectedFlag = selectedMap.also {
                    it[event.type] = !wasSelected
                }
            )
        }?.push()
    }


    private fun initFilters() {
        viewModelScope.launch(Dispatchers.Default) {
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
                        dateMaxDateUnixMilliseconds = SECONDS.toMillis(unixMillisecondLast),
                        dateMinDateUnixMilliSeconds = SECONDS.toMillis(unixMillisecondFirst),
                        dateYearMonthDayAfter = yearMonthStart,
                        dateYearMonthDayBefore = yearMonthEnd,
                        typesWithSelectedFlag = activityTypesWithSelectedFlag
                    )
                )
            }
        }
    }
}