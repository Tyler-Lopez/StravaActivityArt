package com.company.activityart.presentation.loadActivitiesScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.use_case.activities.GetActivitiesByYearUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.NavigateEditArt
import com.company.activityart.presentation.MainDestination.NavigateUp
import com.company.activityart.presentation.loadActivitiesScreen.LoadActivitiesViewEvent.*
import com.company.activityart.presentation.loadActivitiesScreen.LoadActivitiesViewState.LoadError
import com.company.activityart.presentation.loadActivitiesScreen.LoadActivitiesViewState.Loading
import com.company.activityart.util.NavArgSpecification.*
import com.company.activityart.util.Response
import com.company.activityart.util.Response.Error
import com.company.activityart.util.Response.Success
import com.company.activityart.util.doOnError
import com.company.activityart.util.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    private val athleteId: Long = AthleteId.retrieveArg(savedStateHandle) as Long
    private val accessToken: String = AccessToken.retrieveArg(savedStateHandle) as String

    private val activitiesByYear: MutableList<Pair<Int, List<Activity>>> =
        mutableListOf()
    private var activitiesCount = NO_ACTIVITIES_LOADED_COUNT

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

    private suspend fun onNavigateUpClicked() {
        routeTo(NavigateUp)
    }

    private fun onTryAgainClicked() {

    }

    override fun onRouterAttached() {
        if (lastPushedState == null) {
            Loading().push()
            viewModelScope.launch(Dispatchers.IO) {
                loadActivities()
            }
        }
    }

    private suspend fun loadActivities() {
        /** Load activities until complete or
         * returned [Response] is an [Error] **/
        (YEAR_NOW downTo YEAR_START).takeWhile { year ->
            (getActivitiesByYearUseCase(
                accessToken = accessToken,
                athleteId = athleteId,
                year = year,
            ).doOnSuccess {
                activitiesByYear += Pair(year, data)
                activitiesCount += data.size
                Loading(activitiesCount).push()
            }.doOnError {
                LoadError(activitiesCount).push()
                return@loadActivities
            }) is Success
        }
        routeTo(NavigateEditArt(fromLoad = true))
    }
}