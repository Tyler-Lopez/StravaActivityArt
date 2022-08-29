package com.company.activityart.presentation.welcome_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.activityart.Screen.*
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.architecture.StateSender
import com.company.activityart.domain.models.Athlete
import com.company.activityart.domain.models.fullName
import com.company.activityart.domain.use_case.athlete.GetAthleteUseCase
import com.company.activityart.domain.use_case.authentication.ClearAccessTokenUseCase
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewEvent.*
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewState.*
import com.company.activityart.util.Resource.Error
import com.company.activityart.util.Resource.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val clearAccessTokenUseCase: ClearAccessTokenUseCase,
    private val getAthleteAndInsertUseCase: GetAthleteUseCase
) : ViewModel(), EventReceiver<WelcomeScreenViewEvent>, StateSender<WelcomeScreenViewState> {

    // ViewState - observed in the view
    private var _viewState: MutableState<WelcomeScreenViewState> = mutableStateOf(Launch)
    override val viewState: State<WelcomeScreenViewState> = _viewState

    // Received Athlete
    private var accessToken: String? = null
    private val athlete = mutableStateOf<Athlete?>(null)

    override fun onEvent(event: WelcomeScreenViewEvent) {
        when (event) {
            is ClickedAbout -> onClickedAbout(event)
            is ClickedMakeArt -> onClickedMakeArt(event)
            is ClickedLogout -> onClickedLogout(event)
            is LoadAthlete -> onLoadAthlete(event)
        }
    }

    private fun onClickedAbout(event: ClickedAbout) {
        event.navController.navigate(route = About.route)
    }

    private fun onClickedMakeArt(event: ClickedMakeArt) {
        event.navController.navigate(
            FilterYear.withArgs(
                (athlete.value?.athleteId).toString(),
                accessToken ?: error("Access token was not found.")
            )
        )
    }

    private fun onClickedLogout(event: ClickedLogout) {
        viewModelScope.launch {
            _viewState.value = Loading
            clearAccessTokenUseCase()
            event.navController.navigate(route = Login.route) {
                popUpTo(route = Welcome.route + "/{athleteId}/{accessToken}") {
                    inclusive = true
                }
            }
        }
    }

    private fun onLoadAthlete(event: LoadAthlete) {
        _viewState.value = Loading
        viewModelScope.launch {
            accessToken = event.accessToken
            when (val response = getAthleteAndInsertUseCase(event.athleteId, event.accessToken)) {
                is Success -> {
                    response.data.let {
                        athlete.value = it
                        _viewState.value = Standby(it.fullName, it.profilePictureLarge)
                    }
                }
                is Error -> {
                    // Todo, add logic re: parsing exception into error message
                    _viewState.value = LoadError("An error occurred.")
                }
            }
        }
    }
}
