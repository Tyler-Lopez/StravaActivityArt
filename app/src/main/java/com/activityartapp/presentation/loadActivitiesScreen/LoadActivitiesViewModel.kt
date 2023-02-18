package com.activityartapp.presentation.loadActivitiesScreen

import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.domain.models.Athlete
import com.activityartapp.domain.models.Version
import com.activityartapp.domain.useCase.activities.GetActivitiesFromDiskAndRemote
import com.activityartapp.domain.useCase.authentication.GetAthleteFromDiskOrRemote
import com.activityartapp.domain.useCase.version.GetVersionFromRemote
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.MainDestination.*
import com.activityartapp.presentation.errorScreen.ErrorScreenType
import com.activityartapp.presentation.loadActivitiesScreen.LoadActivitiesViewEvent.*
import com.activityartapp.presentation.loadActivitiesScreen.LoadActivitiesViewState.*
import com.activityartapp.util.Response
import com.activityartapp.util.Response.Success
import com.activityartapp.util.classes.ApiError
import com.activityartapp.util.doOnError
import com.activityartapp.util.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class LoadActivitiesViewModel @Inject constructor(
    private val getActivitiesFromDiskAndRemote: GetActivitiesFromDiskAndRemote,
    private val getAthleteFromDiskOrRemote: GetAthleteFromDiskOrRemote,
    private val getVersionFromRemote: GetVersionFromRemote,
) : BaseRoutingViewModel<LoadActivitiesViewState, LoadActivitiesViewEvent, MainDestination>() {

    companion object {
        /** Artificial delay to make the RETRY button feel better when pressed **/
        private const val DELAY_MS = 500L

        private const val DELAY_MS_SUCCESSFULLY_LOADED = 1000L
        private const val NO_ACTIVITIES_COUNT = 0
    }

    override fun onEvent(event: LoadActivitiesViewEvent) {
        when (event) {
            is ClickedContinue -> onClickedContinue()
            is ClickedReconnectWithStrava -> onClickedReconnectWithStrava()
            is ClickedRetry -> onClickedRetry()
            is ClickedReturn -> onClickedReturn()
        }
    }

    private fun onClickedContinue() {
        viewModelScope.launch { routeTo(NavigateEditArt(fromLoad = true)) }
    }

    private fun onClickedReconnectWithStrava() {
        viewModelScope.launch { routeTo(ConnectWithStrava) }
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

        /** Determine from Remote if this version is still supported. **/
        val versionResponse: Response<Version> = getVersionFromRemote()
        val versionSupported: Boolean = versionResponse.data?.isSupported ?: true

        /** If unsupported, show an error to the athlete **/
        if (!versionSupported) {
            routeTo(
                NavigateError(
                    clearNavigationHistory = true,
                    errorScreenType = ErrorScreenType.UNSUPPORTED_VERSION
                )
            )
            return
        }

        /** If we don't have internet to know if the version is supported, make sure we don't access the internet **/
        val internetEnabled: Boolean = versionResponse is Success

        /* Athlete should never return null here */
        val oAuth2 = getOAuth2() ?: error("Athlete is null for an unknown reason...")

        getActivitiesFromDiskAndRemote(
            athlete = oAuth2,
            internetEnabled = internetEnabled,
            onActivitiesLoaded = { newNumberOfActivities ->
                activitiesCount += newNumberOfActivities
                activitiesCount.takeIf { it > NO_ACTIVITIES_COUNT }?.let { Loading(it).push() }
            }
        )
            .doOnError {
                error = ApiError.valueOf(exception)
            }

        when {
            error != null -> error?.let {
                ErrorApi(
                    error = it,
                    totalActivitiesLoaded = activitiesCount,
                    retrying = false
                )
            }?.push()
            activitiesCount == NO_ACTIVITIES_COUNT -> ErrorNoActivities.push()
            else -> {
                Loaded(totalActivitiesLoaded = activitiesCount).push()
                delay(timeMillis = DELAY_MS_SUCCESSFULLY_LOADED)
                routeTo(NavigateEditArt(fromLoad = true))
            }
        }
    }

    private suspend fun getOAuth2(): Athlete? {
        return suspendCoroutine { continuation ->
            viewModelScope.launch(Dispatchers.IO) {
                getAthleteFromDiskOrRemote()
                    .doOnSuccess {
                        continuation.resume(data)
                    }
                    .doOnError {
                        continuation.resume(data)
                    }
            }
        }
    }
}
