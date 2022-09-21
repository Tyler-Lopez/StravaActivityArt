package com.company.activityart.presentation.welcome_screen

import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.models.Athlete
import com.company.activityart.domain.models.fullName
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.domain.use_case.athlete.GetAthleteUseCase
import com.company.activityart.domain.use_case.authentication.ClearAccessTokenUseCase
import com.company.activityart.domain.use_case.authentication.GetAccessTokenUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.welcome_screen.WelcomeViewEvent.*
import com.company.activityart.presentation.welcome_screen.WelcomeViewState.Loading
import com.company.activityart.presentation.welcome_screen.WelcomeViewState.Standby
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val clearAccessTokenUseCase: ClearAccessTokenUseCase,
    private val getActivitiesFromCacheUseCase: GetActivitiesFromCacheUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getAthleteUseCase: GetAthleteUseCase,
) : BaseRoutingViewModel<WelcomeViewState, WelcomeViewEvent, MainDestination>() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            pushState(Loading)
            getAccessToken()
        }
    }
    
    private var athlete: Athlete? = null
    private var accessToken: String? = null

    override fun onEvent(event: WelcomeViewEvent) {
        viewModelScope.launch(Dispatchers.Default) {
            when (event) {
                is ClickedAbout -> onClickedAbout()
                is ClickedMakeArt -> onClickedMakeArt()
                is ClickedLogout -> onClickedLogout()
            }
        }
    }

    private suspend fun onClickedAbout() {
        routeTo(NavigateAbout)
    }

    private suspend fun onClickedMakeArt() {
        /** Either route to screen where activities are loaded into RAM cache
         * or directly to Make Art if RAM cache is already present. */
        routeTo(
            if (getActivitiesFromCacheUseCase().isEmpty()) {
                val athleteId = athlete?.athleteId ?: error("AthleteID missing.")
                val accessToken = accessToken ?: error("Access token missing.")
                NavigateLoadActivities(athleteId, accessToken)
            } else {
                NavigateEditArt(fromLoad = false)
            }
        )
    }

    private suspend fun onClickedLogout() {
        clearAccessTokenUseCase()
        routeTo(NavigateLogin)
    }

    private suspend fun getAccessToken() {
        getAccessTokenUseCase()
            .doOnSuccess {
                accessToken = data.accessToken
                loadAthlete(data.athleteId, data.accessToken)
            }
            .doOnError {
                clearAccessTokenUseCase()
                routeTo(NavigateLogin)
            }

    }

    private suspend fun loadAthlete(
        athleteId: Long,
        accessToken: String
    ) {
        getAthleteUseCase(athleteId, accessToken)
            .doOnSuccess {
                athlete = data
                pushState(
                    Standby(
                        athleteName = data.fullName,
                        athleteImageUrl = data.profilePictureLarge
                    )
                )
            }
            .doOnError { routeTo(NavigateLogin) } // Todo, load error state instead
    }
}