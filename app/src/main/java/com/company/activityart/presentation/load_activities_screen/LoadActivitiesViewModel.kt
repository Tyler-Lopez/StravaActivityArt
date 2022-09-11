package com.company.activityart.presentation.load_activities_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.use_case.activities.GetActivitiesByYearUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.load_activities_screen.LoadActivitiesViewEvent.*
import com.company.activityart.presentation.load_activities_screen.LoadActivitiesViewState.*
import com.company.activityart.util.Resource
import com.company.activityart.util.Resource.*
import com.company.activityart.util.ext.accessToken
import com.company.activityart.util.ext.athleteId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class LoadActivitiesViewModel @Inject constructor(
    private val getActivitiesByYearUseCase: GetActivitiesByYearUseCase,
    savedStateHandle: SavedStateHandle
) : BaseRoutingViewModel<LoadActivitiesViewState, LoadActivitiesViewEvent, MainDestination>() {

    companion object {
        private const val NO_ACTIVITIES_LOADED_COUNT = 0
        private const val YEAR_START = 2018
        private val YEAR_NOW = Year.now().value
    }

    private val athleteId: Long = savedStateHandle.athleteId
    private val accessToken: String = savedStateHandle.accessToken

    private val activitiesByYear: MutableList<Pair<Int, List<Activity>>> =
        mutableListOf()
    private var activitiesCount = NO_ACTIVITIES_LOADED_COUNT

    init {
        Loading(activitiesCount).push()
    }

    override fun onEvent(event: LoadActivitiesViewEvent) {
        viewModelScope.launch {
            when (event) {
                is ContinueClicked -> onContinueClicked()
                is NavigateUpClicked -> onNavigateUpClicked()
                is TryAgainClicked -> onTryAgainClicked()
            }
        }
    }

    private fun onContinueClicked() {

    }

    private fun onNavigateUpClicked() {
        routeTo(NavigateUp)
    }

    private fun onTryAgainClicked() {

    }

    override fun onRouterAttached() {
        /** Load activities only after [Router] is attached as
         * successful load results in automatic navigation to next screen */
        viewModelScope.launch {
            loadActivities()
        }
    }

    private suspend fun loadActivities() {
        /** Load activities until complete or
         * returned [Resource] is an [Error] **/
        (YEAR_NOW downTo YEAR_START).takeWhile { year ->
            getActivitiesByYearUseCase(
                accessToken = accessToken,
                athleteId = athleteId,
                year = year,
            ).doOnSuccess {
                activitiesByYear += Pair(year, data)
                activitiesCount += data.size
                Loading(activitiesCount).push()
            }.doOnError {
                LoadError(activitiesCount).push()
                return
            } is Success
        }
        routeTo(NavigateMakeArt(athleteId, accessToken))
    }
}