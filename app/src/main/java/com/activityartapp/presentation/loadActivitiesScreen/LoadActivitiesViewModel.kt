package com.activityartapp.presentation.loadActivitiesScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.use_case.activities.GetActivitiesByYearUseCase
import com.activityartapp.domain.use_case.activities.InsertActivitiesIntoCacheUseCase
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.MainDestination.NavigateEditArt
import com.activityartapp.presentation.MainDestination.NavigateUp
import com.activityartapp.presentation.loadActivitiesScreen.LoadActivitiesViewEvent.*
import com.activityartapp.presentation.loadActivitiesScreen.LoadActivitiesViewState.LoadErrorNoInternet
import com.activityartapp.presentation.loadActivitiesScreen.LoadActivitiesViewState.Loading
import com.activityartapp.util.NavArgSpecification.*
import com.activityartapp.util.Response
import com.activityartapp.util.Response.Error
import com.activityartapp.util.Response.Success
import com.activityartapp.util.doOnError
import com.activityartapp.util.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class LoadActivitiesViewModel @Inject constructor(
    private val getActivitiesByYearUseCase: GetActivitiesByYearUseCase,
    private val insertActivitiesIntoCacheUseCase: InsertActivitiesIntoCacheUseCase,
    savedStateHandle: SavedStateHandle
) : BaseRoutingViewModel<LoadActivitiesViewState, LoadActivitiesViewEvent, MainDestination>() {

    companion object {
        private const val YEAR_START = 2018
        private val YEAR_NOW = Year.now().value
    }

    private val athleteId: Long = AthleteId.rawArg(savedStateHandle).toLong()
    private val accessToken: String = AccessToken.rawArg(savedStateHandle)

    private val activitiesByYear: MutableList<Pair<Int, List<Activity>>> =
        mutableListOf()

    override fun onEvent(event: LoadActivitiesViewEvent) {
        when (event) {
            is ClickedContinue -> onClickedContinue()
            is ClickedRetry -> onClickedRetry()
            is ClickedReturn -> onClickedReturn()
        }
    }

    private fun onClickedContinue() {
        viewModelScope.launch { routeTo(NavigateEditArt(fromLoad = true)) }
    }

    private fun onClickedRetry() {
        (lastPushedState as? LoadErrorNoInternet)?.copy(retrying = true)?.push()
        viewModelScope.launch(Dispatchers.IO) { loadActivities() }
    }

    private fun onClickedReturn() {
        viewModelScope.launch { routeTo(NavigateUp) }
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
        var noInternetError = false
        var activitiesCount = 0

        /** Load activities until complete or
         * returned [Response] is an [Error] **/
        (YEAR_NOW downTo YEAR_START).takeWhile { year ->
            val response = (getActivitiesByYearUseCase(
                accessToken = accessToken,
                athleteId = athleteId,
                year = year,
            ).doOnSuccess {

                /** Add data to Singleton cache for future access **/
                insertActivitiesIntoCacheUseCase(year, data)

                activitiesByYear += Pair(year, data)
                activitiesCount += data.size
                if (lastPushedState == null || lastPushedState is Loading) Loading(activitiesCount).push()
            }.doOnError {
                when (exception) {
                    is UnknownHostException -> {
                        noInternetError = true
                    }
                    else -> {
                        // TODO, Handle case of other type of exception
                        // Right now will just be loading forever
                        return@loadActivities
                    }
                }
            })
            /** If response is a Success or an Error due to no internet, keep loading activities **/
            response is Success || (response as? Error)?.exception is UnknownHostException
        }


        if (noInternetError) {
            LoadErrorNoInternet(totalActivitiesLoaded = activitiesCount, retrying = false).push()
        } else {
            routeTo(NavigateEditArt(fromLoad = true))
        }
    }
}