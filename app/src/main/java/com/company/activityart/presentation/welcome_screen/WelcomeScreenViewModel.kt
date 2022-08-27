package com.company.activityart.presentation.welcome_screen

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.activityart.data.entities.AthleteEntity
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewState.*
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewEvent.*
import com.company.activityart.domain.use_case.AthleteUseCases
import com.company.activityart.domain.use_case.AuthenticationUseCases
import com.company.activityart.util.Resource.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    athleteUseCases: AthleteUseCases,
    private val authenticationUseCases: AuthenticationUseCases
) : ViewModel() {

    // Use cases
    private val getAthleteUseCase = athleteUseCases.getAthleteUseCase
    private val setAthleteUseCase = athleteUseCases.setAthleteUseCase

    // ViewState - observed in the view
    var viewState: MutableState<WelcomeScreenViewState> = mutableStateOf(Launch)
        private set

    // Received Athlete
    private val athlete = mutableStateOf<AthleteEntity?>(null)
    private val athleteImageUrl: String
        get() = athlete.value?.profilePictureLarge ?: "via.placeholder.com/128"
    private val athleteName: String
        get() = "${athlete.value?.firstName} ${athlete.value?.lastName}"

    // Todo, define architecture for this to be an override fun
    fun onEvent(event: WelcomeScreenViewEvent) {
        when (event) {
            is LoadAthlete -> onLoadAthlete(event)
        }
    }

    private fun onLoadAthlete(event: LoadAthlete) {
        viewState.value = Loading
        viewModelScope.launch {
            when (val response =
                getAthleteUseCase.getAthlete(
                    athleteId = event.athleteId,
                    code = event.accessToken
                )) {
                is Success -> {
                    val data = response.data
                    athlete.value = data
                    setAthleteUseCase.setAthlete(context = context, athleteEntity = data)
                    viewState.value = Standby(athleteName, athleteImageUrl)
                }
                is Error -> {
                 //   logout(context)
                }
            }
        }
    }

    private fun logout(
        context: Context
    ) {
        viewState.value = Loading
        viewModelScope.launch {
            authenticationUseCases.clearAccessTokenUseCase.clearAccessToken(context = context)
            viewState.value = Logout
        }
    }

}
