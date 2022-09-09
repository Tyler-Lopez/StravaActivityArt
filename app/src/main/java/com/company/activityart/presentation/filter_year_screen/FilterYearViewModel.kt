package com.company.activityart.presentation.filter_year_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.use_case.activities.GetActivitiesByYearUseCase
import com.company.activityart.domain.use_case.athlete.GetLastCachedYearMonthsUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.NavigateUp
import com.company.activityart.presentation.filter_year_screen.FilterYearViewEvent.ContinueClicked
import com.company.activityart.presentation.filter_year_screen.FilterYearViewEvent.NavigateUpClicked
import com.company.activityart.presentation.filter_year_screen.FilterYearViewState.Loading
import com.company.activityart.presentation.filter_year_screen.FilterYearViewState.Standby
import com.company.activityart.util.Resource
import com.company.activityart.util.ext.accessToken
import com.company.activityart.util.ext.athleteId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class FilterYearViewModel @Inject constructor(
    private val getActivitiesByYearUseCase: GetActivitiesByYearUseCase,
    private val getAthleteCachedMonthsByYearUseCase: GetLastCachedYearMonthsUseCase,
    savedStateHandle: SavedStateHandle
) : BaseRoutingViewModel<FilterYearViewState, FilterYearViewEvent, MainDestination>() {

    companion object {
        private const val YEAR_START = 2018
        private val YEAR_NOW = Year.now().value
    }

    private val athleteId: Long = savedStateHandle.athleteId
    private val accessToken: String = savedStateHandle.accessToken

    private val activitiesByYear: MutableList<Pair<Int, List<Activity>>> =
        mutableListOf()

    init {
        pushState(Loading)
        viewModelScope.launch {
            loadActivities()
        }
    }

    override fun onEvent(event: FilterYearViewEvent) {
        viewModelScope.launch {
            when (event) {
                is ContinueClicked -> onContinueClicked()
                is NavigateUpClicked -> onNavigateUpClicked()
            }
        }
    }

    private fun onContinueClicked() {

    }

    private fun onNavigateUpClicked() {
        routeTo(NavigateUp)
    }

    override fun onRouterAttached() {

    }

    private suspend fun loadActivities() {
        val cachedYearMonths = getAthleteCachedMonthsByYearUseCase(athleteId)
        (YEAR_NOW downTo YEAR_START).takeWhile {
            getActivitiesByYearUseCase(
                accessToken = accessToken,
                athleteId = athleteId,
                year = it,
                lastCachedMonth = cachedYearMonths[it] ?: -1
            )
                .doOnSuccess {
                    activitiesByYear += Pair(it, data)
                    onActivitiesLoaded(isLoading = true)
                }
                .doOnError {
                    onActivitiesLoaded(loadError = true)
                    return
                }
                .run { this is Resource.Success }
        }
        onActivitiesLoaded(isLoading = false)
    }

    private fun onActivitiesLoaded(
        isLoading: Boolean = false,
        loadError: Boolean = false
    ) {
        // Todo, add another state for when no activities exist at all
        // which should be checked when loading == false && error == false
        Standby(
            isLoading = isLoading,
            loadErrorOccurred = loadError,
            activitiesByYear = activitiesByYear
        ).push()
    }
}