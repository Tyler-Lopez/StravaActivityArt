package com.company.activityart.presentation.select_years_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.use_case.activities.GetActivitiesByYearUseCase
import com.company.activityart.domain.use_case.athlete.GetLastCachedYearMonthsUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.NavigateUp
import com.company.activityart.presentation.select_years_screen.SelectYearsViewEvent.ContinueClicked
import com.company.activityart.presentation.select_years_screen.SelectYearsViewEvent.NavigateUpClicked
import com.company.activityart.presentation.select_years_screen.SelectYearsViewState.Loading
import com.company.activityart.presentation.select_years_screen.SelectYearsViewState.Standby
import com.company.activityart.util.Resource
import com.company.activityart.util.ext.accessToken
import com.company.activityart.util.ext.athleteId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class SelectYearsViewModel @Inject constructor(
    private val getActivitiesByYearUseCase: GetActivitiesByYearUseCase,
    private val getAthleteCachedMonthsByYearUseCase: GetLastCachedYearMonthsUseCase,
    savedStateHandle: SavedStateHandle
) : BaseRoutingViewModel<SelectYearsViewState, SelectYearsViewEvent, MainDestination>() {

    companion object {
        private const val YEAR_START = 2021
        private val YEAR_NOW = Year.now().value
    }

    private val athleteId: Long = savedStateHandle.athleteId
    private val accessToken: String = savedStateHandle.accessToken

    private val activitiesByYear: MutableList<Pair<Int, List<Activity>>> =
        mutableListOf()

    init {
        pushState(Loading(0))
    }

    override fun onEvent(event: SelectYearsViewEvent) {
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
        viewModelScope.launch {
            loadActivities()
        }
    }

    private suspend fun loadActivities() {
        (YEAR_NOW downTo YEAR_START).takeWhile {
            getActivitiesByYearUseCase(
                accessToken = accessToken,
                athleteId = athleteId,
                year = it,
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
            activitiesCountByYear = activitiesByYear.map {
                it.first to it.second.size
            }
        ).push()
    }
}