package com.company.activityart.presentation.welcome_screen

import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.models.Athlete
import com.company.activityart.domain.models.fullName
import com.company.activityart.domain.use_case.athlete.GetAthleteUseCase
import com.company.activityart.domain.use_case.authentication.ClearAccessTokenUseCase
import com.company.activityart.domain.use_case.authentication.GetAccessTokenUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.welcome_screen.WelcomeViewEvent.*
import com.company.activityart.presentation.welcome_screen.WelcomeViewState.Loading
import com.company.activityart.presentation.welcome_screen.WelcomeViewState.Standby
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val clearAccessTokenUseCase: ClearAccessTokenUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getAthleteUseCase: GetAthleteUseCase,
) : BaseRoutingViewModel<WelcomeViewState, WelcomeViewEvent, MainDestination>() {

    init {
        pushState(Loading)
        viewModelScope.launch {
            getAccessToken()
        }
    }

    private var athlete: Athlete? = null
    private var accessToken: String? = null

    override fun onEvent(event: WelcomeViewEvent) {
        viewModelScope.launch {
            when (event) {
                is ClickedAbout -> onClickedAbout()
                is ClickedMakeArt -> onClickedMakeArt()
                is ClickedLogout -> onClickedLogout()
            }
        }
    }

    private fun onClickedAbout() {
        routeTo(NavigateAbout)
    }

    private fun onClickedMakeArt() {
        routeTo(
            NavigateLoadActivities(
                athlete?.athleteId ?: error("AthleteID missing."),
                accessToken ?: error("Access token missing.")
            )
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