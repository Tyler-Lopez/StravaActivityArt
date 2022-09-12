package com.company.activityart.presentation.make_art_screen.subscreens.filters

import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.domain.use_case.filters.InsertFiltersIntoCacheUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.make_art_screen.subscreens.filters.EditArtFiltersViewEvent.DistanceRangeChanged
import com.company.activityart.presentation.make_art_screen.subscreens.filters.EditArtFiltersViewState.LoadingFilters
import com.company.activityart.presentation.make_art_screen.subscreens.filters.EditArtFiltersViewState.Standby
import com.company.activityart.util.TimeUtils
import com.company.activityart.util.YearMonthDay
import com.company.activityart.util.ext.closestValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditArtFiltersViewModel @Inject constructor(
    private val insertFiltersIntoCacheUseCase: InsertFiltersIntoCacheUseCase,
    private val timeUtils: TimeUtils,
    cacheUseCase: GetActivitiesFromCacheUseCase,
) : BaseRoutingViewModel<EditArtFiltersViewState, EditArtFiltersViewEvent, MainDestination>() {

    private val activities: List<Activity> =
        cacheUseCase().flatMap { it.value }
    private val activitiesUnixSeconds: List<Long> =
        activities.map { timeUtils.iso8601StringToUnixSecond(it.iso8601LocalDate) }

    private lateinit var dateFilters: DateFilters


    init {
        pushState(LoadingFilters)
        viewModelScope.launch {
            initDateFilters()
            pushFilters()
        }
    }

    override fun onEvent(event: EditArtFiltersViewEvent) {
        when (event) {
            is DistanceRangeChanged -> onDistanceRangeChanged(event)
        }
    }

    private fun onDistanceRangeChanged(event: DistanceRangeChanged) {
        event.newUnixSecondsRange.apply {
            val closestSecondStart =
                activitiesUnixSeconds.closestValue(start).toFloat()
            val closestSecondEnd =
                activitiesUnixSeconds.closestValue(endInclusive).toFloat()
            dateFilters = dateFilters.copy(
                dateEarliestSelected =
                timeUtils.unixSecondToYearMonthDay(closestSecondStart.toLong()),
                dateLatestSelected =
                timeUtils.unixSecondToYearMonthDay(closestSecondEnd.toLong()),
                unixSecondsRangeSelected = closestSecondStart..closestSecondEnd
            )
        }
        pushFilters()
    }

    private fun pushFilters() {
        pushState(
            Standby(
                dateEarliestSelected = dateFilters.dateEarliestSelected,
                dateLatestSelected = dateFilters.dateLatestSelected,
                unixSecondsRangeSelected = dateFilters.unixSecondsRangeSelected,
                unixSecondsRangeTotal = dateFilters.unixSecondsRangeTotal,
                distanceMax = 0.0,
                distanceMin = 1.0,
                selectedActivitiesCount = activities.size
            )
        )
        insertFiltersIntoCacheUseCase(
            startDateUnixSeconds = dateFilters.unixSecondsRangeSelected.start.toLong(),
            endDateUnixSeconds = dateFilters.unixSecondsRangeSelected.endInclusive.toLong()
        )
    }

    private data class DateFilters(
        val dateEarliestSelected: YearMonthDay,
        val dateLatestSelected: YearMonthDay,
        val unixSecondsRangeSelected: ClosedFloatingPointRange<Float>,
        val unixSecondsRangeTotal: ClosedFloatingPointRange<Float>
    )

    private fun initDateFilters() {

        val unixSecondTotalFirst = activitiesUnixSeconds.min()
        val unixSecondTotalLast = activitiesUnixSeconds.max()
        val unixSecondsTotalRange = unixSecondTotalFirst.toFloat()..unixSecondTotalLast.toFloat()

        val earliestYearMonthDay = timeUtils.unixSecondToYearMonthDay(unixSecondTotalFirst)
        val latestYearMonthDay = timeUtils.unixSecondToYearMonthDay(unixSecondTotalLast)

        dateFilters = DateFilters(
            dateEarliestSelected = earliestYearMonthDay,
            dateLatestSelected = latestYearMonthDay,
            unixSecondsRangeSelected = unixSecondsTotalRange,
            unixSecondsRangeTotal = unixSecondsTotalRange,
        )
    }

}