package com.company.activityart.presentation.welcome_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.models.Athlete
import com.company.activityart.domain.models.fullName
import com.company.activityart.domain.use_case.athlete.GetAthleteUseCase
import com.company.activityart.domain.use_case.authentication.ClearAccessTokenUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewEvent.*
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewState.*
import com.company.activityart.util.Resource.Error
import com.company.activityart.util.Resource.Success
import com.company.activityart.util.Screen.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val clearAccessTokenUseCase: ClearAccessTokenUseCase,
    private val getAthleteAndInsertUseCase: GetAthleteUseCase
) : BaseRoutingViewModel<WelcomeScreenViewState, WelcomeScreenViewEvent, MainDestination>() {

    init {
        pushState(Loading)
    }

    // Received Athlete
    private var accessToken: String? = null
    private val athlete = mutableStateOf<Athlete?>(null)

    override fun onEvent(event: WelcomeScreenViewEvent) {
        when (event) {
            is ClickedAbout -> onClickedAbout()
            is ClickedMakeArt -> onClickedMakeArt()
            is ClickedLogout -> onClickedLogout()
            is LoadAthlete -> onLoadAthlete()
        }
    }

    private fun onClickedAbout() {
        routeTo(NavigateAbout)
    }

    private fun onClickedMakeArt() {
        routeTo(NavigateMakeArt)
        /*
        event.navController.navigate(
            FilterYear.withArgs(
                (athlete.value?.athleteId).toString(),
                accessToken ?: error("Access token was not found.")
            )
        )

         */
    }

    private fun onClickedLogout() {
        viewModelScope.launch {
            pushState(Loading)
            clearAccessTokenUseCase()
            routeTo(NavigateLogin)
            /*
            event.navController.navigate(route = Login.route) {
                popUpTo(route = Welcome.route + "/{athleteId}/{accessToken}") {
                    inclusive = true
                }
            }

             */
        }
    }

    private fun onLoadAthlete() {
        pushState(Loading)
        viewModelScope.launch {
            /*
            accessToken = event.accessToken
            when (val response = getAthleteAndInsertUseCase(event.athleteId, event.accessToken)) {
                is Success -> {
                    response.data.let {
                        athlete.value = it
                       pushState(Standby(it.fullName, it.profilePictureLarge))
                    }
                }
                is Error -> {
                    // Todo, add logic re: parsing exception into error message
                    pushState(LoadError("An error occurred."))
                }
            }

             */
        }
    }
}
