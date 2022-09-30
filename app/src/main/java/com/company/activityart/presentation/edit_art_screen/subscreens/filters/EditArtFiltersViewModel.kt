package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import com.company.activityart.architecture.BaseChildViewModel
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.ArtMutatingEvent.FilterDateChanged
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.ArtMutatingEvent.FilterTypeChanged.FilterTypeAdded
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.ArtMutatingEvent.FilterTypeChanged.FilterTypeRemoved
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.DateChanged
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.DateChanged.DateChangedAfter
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.DateChanged.DateChangedBefore
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent.TypeToggleFlipped
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewState.Standby
import com.company.activityart.util.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

@HiltViewModel
class EditArtFiltersViewModel @Inject constructor(
    private val getActivitiesFromCacheUseCase: GetActivitiesFromCacheUseCase,
    private val timeUtils: TimeUtils
) : BaseChildViewModel<
        EditArtFiltersViewState,
        EditArtFiltersViewEvent,
        EditArtViewEvent
        >() {

    init {
        initFilters()
    }

    override fun onEvent(event: EditArtFiltersViewEvent) {
        when (event) {
            is DateChanged -> onDateChanged(event)
            is TypeToggleFlipped -> onTypeToggleFlipped(event)
        }
    }

    private fun onDateChanged(event: DateChanged) {
        /*
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
                    MILLISECONDS.toSeconds(newAfter.unixMs),
                    MILLISECONDS.toSeconds(newBefore.unixMs)
                )
            )
            copy(
                dateYearMonthDayBefore = newBefore,
                dateYearMonthDayAfter = newAfter
            )
        }?.push()

         */
    }

    private fun onTypeToggleFlipped(event: TypeToggleFlipped) {
        (lastPushedState as? Standby)?.run {
            val selectedMap = typesWithSelectedFlag.toMutableMap()
            val wasSelected = selectedMap[event.type] ?: error("Type missing")
            val parentEvent = if (wasSelected) {
                FilterTypeAdded(event.type)
            } else {
                FilterTypeRemoved(event.type)
            }
            onParentEvent(parentEvent)
            copy(
                typesWithSelectedFlag = selectedMap.also {
                    it[event.type] = !wasSelected
                }
            )
        }?.push()
    }


    private fun initFilters() {
/*
        viewModelScope.launch(Dispatchers.Default) {
            val activities = getActivitiesFromCacheUseCase().flatMap { it.value }
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
                    activities.map { it.type }.distinct().associateWith { true }

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

 */
    }
}