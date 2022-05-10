package com.company.athleteapiart.presentation.welcome_screen

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.entities.AthleteEntity
import com.company.athleteapiart.domain.use_case.AthleteUseCases
import com.company.athleteapiart.domain.use_case.AuthenticationUseCases
import com.company.athleteapiart.util.Resource.*
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

    // State - observed in the view
    val screenState = mutableStateOf(WelcomeScreenState.LAUNCH)

    // Received Athlete
    private val athlete = mutableStateOf<AthleteEntity?>(null)
    val athleteImageUrl: String
        get() = athlete.value?.profilePictureLarge ?: "via.placeholder.com/128"
    val athleteName: String
        get() = "${athlete.value?.firstName} ${athlete.value?.lastName}"

    fun getAthlete(
        context: Context,
        athleteId: Long,
        accessToken: String
    ) {
        screenState.value = WelcomeScreenState.LOADING
        viewModelScope.launch {
            when (val response =
                getAthleteUseCase.getAthlete(context = context, athleteId = athleteId, code = accessToken)) {
                is Success -> {
                    val data = response.data
                    athlete.value = data
                    setAthleteUseCase.setAthlete(context = context, athleteEntity = data)
                    screenState.value = WelcomeScreenState.STANDBY
                }
                is Error -> {
                    logout(context)
                }
            }
        }
    }

    fun logout(
        context: Context
    ) {
        screenState.value = WelcomeScreenState.LOADING
        viewModelScope.launch {
            authenticationUseCases.clearAccessTokenUseCase.clearAccessToken(context = context)
            screenState.value = WelcomeScreenState.LOGOUT
        }
    }
}
