package com.company.activityart.presentation.welcome_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import com.company.activityart.util.Screen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


//@HiltViewModel
class WelcomeScreenViewModel @AssistedInject constructor(
    @Assisted private val help: String,
    private val clearAccessTokenUseCase: ClearAccessTokenUseCase,
    private val getAthleteUseCase: GetAthleteUseCase
) : BaseRoutingViewModel<WelcomeScreenViewState, WelcomeScreenViewEvent, MainDestination>() {

    companion object {
        fun newInstance(
            factory: Factory,
            help: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory.create(help) as T
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(help: String): WelcomeScreenViewModel
    }

    init {
        pushState(Loading)
        println("HERE HELP IS $help")
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
