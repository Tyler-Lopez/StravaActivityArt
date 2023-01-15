package com.activityartapp.presentation.welcomeScreen

import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.activityart.domain.models.Athlete
import com.activityartapp.domain.models.OAuth2
import com.activityartapp.domain.models.fullName
import com.activityartapp.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.activityartapp.domain.use_case.athlete.GetAthleteUseCase
import com.activityartapp.domain.use_case.athleteUsage.GetAthleteUsage
import com.activityartapp.domain.use_case.athleteUsage.IncrementAthleteUsage
import com.activityartapp.domain.use_case.authentication.ClearAccessTokenUseCase
import com.activityartapp.domain.use_case.authentication.GetAccessTokenUseCase
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.MainDestination.*
import com.activityartapp.presentation.welcomeScreen.WelcomeViewEvent.*
import com.activityartapp.util.Response
import com.activityartapp.util.classes.ApiError
import com.activityartapp.util.doOnError
import com.activityartapp.util.doOnSuccess
import com.activityartapp.util.enums.ErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
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
    private val getAthleteUsage: GetAthleteUsage,
    private val incrementUsage: IncrementAthleteUsage
) : BaseRoutingViewModel<WelcomeViewState, WelcomeViewEvent, MainDestination>() {

    companion object {
        /** Artificial delay to make the RETRY button feel better when pressed **/
        private const val DELAY_MS = 500L
    }

    private lateinit var accessToken: String
    private lateinit var athleteId: String

    init {
        initScreen()

    }

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
                // TODO, come back to this, removed because we now add to cache on partial load...
                //    if (getActivitiesFromCacheUseCase().isEmpty()) {
                NavigateLoadActivities(athleteId, accessToken)
                //  } else {
                //        NavigateEditArt(fromLoad = false)
                //   }
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
        (lastPushedState as? WelcomeViewState.Error)?.copy(retrying = true)?.push()
        viewModelScope.launch {
            delay(DELAY_MS)
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
                viewModelScope.launch(Dispatchers.IO) {
             //       incrementUsage(it.athleteId)
                }
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
        when (val apiError = ApiError.valueOf(exception)) {
            is ApiError.Unauthorized -> {
                viewModelScope.launch {
                    // The athlete has de-authorized our app or some other error
                    clearAccessTokenUseCase()
                    routeTo(NavigateLogin)
                }
            }
            is ApiError.UserFacingError -> {
                WelcomeViewState.Error(
                    error = apiError,
                    retrying = false
                ).push()
            }
        }
    }
}