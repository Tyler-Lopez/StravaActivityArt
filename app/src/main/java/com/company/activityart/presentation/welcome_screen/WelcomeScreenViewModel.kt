package com.company.activityart.presentation.welcome_screen

import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.models.fullName
import com.company.activityart.domain.use_case.athlete.GetAthleteUseCase
import com.company.activityart.domain.use_case.authentication.ClearAccessTokenUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewEvent.*
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewState.Loading
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewState.Standby
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val clearAccessTokenUseCase: ClearAccessTokenUseCase,
    private val getAthleteUseCase: GetAthleteUseCase
) : BaseRoutingViewModel<WelcomeScreenViewState, WelcomeScreenViewEvent, MainDestination>() {

    init {
        pushState(Loading)
    }

    override fun onEvent(event: WelcomeScreenViewEvent) {
        when (event) {
            is ClickedAbout -> onClickedAbout()
            is ClickedMakeArt -> onClickedMakeArt()
            is ClickedLogout -> onClickedLogout()
        }
    }

    private fun onClickedAbout() {
        routeTo(NavigateAbout)
    }

    private fun onClickedMakeArt() {
        routeTo(NavigateMakeArt)
    }

    private fun onClickedLogout() {
        viewModelScope.launch {
            clearAccessTokenUseCase()
            routeTo(NavigateLogin)
        }
    }

    override fun onRouterAttached() {
        loadAthlete()
    }

    private fun loadAthlete() {
        viewModelScope.launch {
            getAthleteUseCase()
                .doOnSuccess {
                    pushState(
                        Standby(
                            athleteName = data.fullName,
                            athleteImageUrl = data.profilePictureLarge
                        )
                    )
                }
                .doOnError { routeTo(NavigateLogin) }
        }
    }
}
