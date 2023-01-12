package com.activityartapp.presentation.welcomeScreen

import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.activityart.domain.models.Athlete
import com.activityartapp.domain.models.OAuth2
import com.activityartapp.domain.models.fullName
import com.activityartapp.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.activityartapp.domain.use_case.athlete.GetAthleteUseCase
import com.activityartapp.domain.use_case.authentication.ClearAccessTokenUseCase
import com.activityartapp.domain.use_case.authentication.GetAccessTokenUseCase
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.MainDestination.*
import com.activityartapp.presentation.welcomeScreen.WelcomeViewEvent.*
import com.activityartapp.util.Response
import com.activityartapp.util.doOnError
import com.activityartapp.util.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val clearAccessTokenUseCase: ClearAccessTokenUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getActivitiesFromCacheUseCase: GetActivitiesFromCacheUseCase,
    private val getAthleteUseCase: GetAthleteUseCase,
) : BaseRoutingViewModel<WelcomeViewState, WelcomeViewEvent, MainDestination>() {

    private lateinit var accessToken: String
    private lateinit var athleteId: String

    init { initScreen() }

    override fun onEvent(event: WelcomeViewEvent) {
        when (event) {
            is ClickedAbout -> onClickedAbout()
            is ClickedMakeArt -> onClickedMakeArt()
            is ClickedLogout -> onClickedLogout()
            is ClickedRetryConnection -> onClickedRetryConnection()
        }
    }

    private fun onClickedAbout() {
        viewModelScope.launch {
            routeTo(NavigateAbout)
        }
    }

    private fun onClickedMakeArt() {
        /** Either route to screen where activities are loaded into RAM cache
         * or directly to Make Art if RAM cache is already present. */
        viewModelScope.launch {
            routeTo(
                if (getActivitiesFromCacheUseCase().isEmpty()) {
                    NavigateLoadActivities(athleteId, accessToken)
                } else {
                    NavigateEditArt(fromLoad = false)
                }
            )
        }
    }

    private fun onClickedLogout() {
        viewModelScope.launch {
            clearAccessTokenUseCase()
            routeTo(NavigateLogin)
        }
    }

    private fun onClickedRetryConnection() {
        viewModelScope.launch {
            WelcomeViewState.NoInternet(retrying = true).push()
            initScreen()
        }
    }

    private fun initScreen() {
        viewModelScope.launch(Dispatchers.IO) {
            val oAuth2 = getOAuth2()
            val athlete = oAuth2?.getAthlete()
            athlete?.let {
                athleteId = it.athleteId.toString()
                WelcomeViewState.Standby(
                    athleteName = it.fullName,
                    athleteImageUrl = it.profilePictureLarge
                ).push()
            }
        }
    }

    private suspend fun getOAuth2(): OAuth2? {
        return suspendCoroutine { continuation ->
            viewModelScope.launch(Dispatchers.IO) {
                getAccessTokenUseCase()
                    .doOnSuccess {
                        accessToken = data.accessToken
                        continuation.resume(data)
                    }
                    .doOnError {
                        continuation.resume(null)
                        handleError()
                    }
            }
        }
    }

    private suspend fun OAuth2.getAthlete(): Athlete? {
        return suspendCoroutine { continuation ->
            viewModelScope.launch(Dispatchers.IO) {
                getAthleteUseCase(athleteId, accessToken)
                    .doOnSuccess {
                        continuation.resume(data)
                    }
                    .doOnError {
                        continuation.resume(null)
                        handleError()
                    }
            }
        }
    }

    private fun <T> Response.Error<T>.handleError() {
        if (exception is UnknownHostException) {
            // The access token is in need of refresh & we are w/o internet
            WelcomeViewState.NoInternet(retrying = false).push()
        } else {
            viewModelScope.launch {
                // The athlete has de-authorized our app or some other error
                clearAccessTokenUseCase()
                routeTo(NavigateLogin)
            }
        }
    }
}