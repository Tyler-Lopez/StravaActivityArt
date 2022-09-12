package com.company.activityart.presentation.make_art_screen.subscreens.filters

import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.make_art_screen.subscreens.filters.EditArtFiltersViewState.*
import com.company.activityart.util.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditArtViewModel @Inject constructor(
    cacheUseCase: GetActivitiesFromCacheUseCase,
    private val timeUtils: TimeUtils
) : BaseRoutingViewModel<EditArtFiltersViewState, EditArtFiltersViewEvent, MainDestination>() {

    private val activities: List<Activity> = cacheUseCase().flatMap { it.value }

    init {
        pushState(LoadingFilters)
        viewModelScope.launch {
            computeFilters()
        }
    }

    override fun onEvent(event: EditArtFiltersViewEvent) {
        TODO("Not yet implemented")
    }

    private suspend fun computeFilters() {
        activities.apply {

            val unixSeconds = map {
                timeUtils.iso8601StringToUnixSecond(it.iso8601LocalDate)
            }
            val dateEarliest = timeUtils.unixSecondToYearMonth(unixSeconds.min())
            val dateLatest = timeUtils.unixSecondToYearMonth(unixSeconds.max())
            val distanceMin = minOf { it.distance }
            val distanceMax = maxOf { it.distance }

            delay(3000)
            pushState(
                Standby(
                    yearMonthEarliest = dateEarliest,
                    yearMonthLatest = dateLatest,
                    distanceMax = distanceMax,
                    distanceMin = distanceMin,
                    selectedActivitiesCount = size
                )
            )
        }
    }

}