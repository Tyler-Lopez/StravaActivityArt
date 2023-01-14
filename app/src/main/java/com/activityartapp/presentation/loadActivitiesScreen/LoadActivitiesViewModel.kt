package com.activityartapp.presentation.loadActivitiesScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.use_case.activities.GetActivitiesByYearUseCase
import com.activityartapp.domain.use_case.activities.InsertActivitiesIntoCacheUseCase
import com.activityartapp.domain.use_case.authentication.ClearAccessTokenUseCase
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.MainDestination.NavigateEditArt
import com.activityartapp.presentation.MainDestination.NavigateUp
import com.activityartapp.presentation.loadActivitiesScreen.LoadActivitiesViewEvent.*
import com.activityartapp.presentation.loadActivitiesScreen.LoadActivitiesViewState.*
import com.activityartapp.util.NavArgSpecification.AccessToken
import com.activityartapp.util.NavArgSpecification.AthleteId
import com.activityartapp.util.Response
import com.activityartapp.util.Response.Error
import com.activityartapp.util.Response.Success
import com.activityartapp.util.classes.ApiError
import com.activityartapp.util.doOnError
import com.activityartapp.util.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class LoadActivitiesViewModel @Inject constructor(
    private val getActivitiesByYearUseCase: GetActivitiesByYearUseCase,
    private val clearAccessTokenUseCase: ClearAccessTokenUseCase,
    private val insertActivitiesIntoCacheUseCase: InsertActivitiesIntoCacheUseCase,
    savedStateHandle: SavedStateHandle
) : BaseRoutingViewModel<LoadActivitiesViewState, LoadActivitiesViewEvent, MainDestination>() {

    companion object {
        /** Artificial delay to make the RETRY button feel better when pressed **/
        private const val DELAY_MS = 500L
        private const val NO_ACTIVITIES_COUNT = 0
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
        (lastPushedState as? ErrorApi)?.copy(retrying = true)?.push()
        viewModelScope.launch(Dispatchers.IO) {
            delay(DELAY_MS)
            loadActivities()
        }
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
        var activitiesCount = NO_ACTIVITIES_COUNT
        var error: ApiError? = null

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
                error = ApiError.valueOf(exception)
            })
            /** If response is a Success or an ApiError due to no internet, keep loading activities **/
            response is Success || (response as? Error)?.exception is UnknownHostException
        }

        when {
            error != null -> if (error is ApiError.UserFacingError) {
                ErrorApi(
                    error = error as ApiError.UserFacingError,
                    totalActivitiesLoaded = activitiesCount,
                    retrying = false
                ).push()
            } else {
                // The athlete has de-authorized our app
                clearAccessTokenUseCase()
                routeTo(MainDestination.NavigateLogin)
            }
            activitiesCount == NO_ACTIVITIES_COUNT -> ErrorNoActivities.push()
            else -> routeTo(NavigateEditArt(fromLoad = true))
        }
    }
}