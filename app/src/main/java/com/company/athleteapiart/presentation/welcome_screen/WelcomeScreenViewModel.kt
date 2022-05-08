package com.company.athleteapiart.presentation.welcome_screen

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.entities.AthleteEntity
import com.company.athleteapiart.domain.use_case.AthleteUseCases
import com.company.athleteapiart.domain.use_case.AuthenticationUseCases
import com.company.athleteapiart.domain.use_case.clear_access_token.ClearAccessTokenUseCase
import com.company.athleteapiart.presentation.login_screen.LoginScreenState
import com.company.athleteapiart.util.Resource.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val athleteUseCases: AthleteUseCases,
    private val authenticationUseCases: AuthenticationUseCases
) : ViewModel() {

    // Use cases
    private val getAthleteUseCase = athleteUseCases.getAthleteUseCase
    private val setAthleteUseCase = athleteUseCases.setAthleteUseCase

    // State - observed in the view
    val screenState = mutableStateOf(WelcomeScreenState.LAUNCH)

    // Received Athlete
    var athlete = mutableStateOf<AthleteEntity?>(null)

    fun getAthlete(
        context: Context,
        accessToken: String
    ) {
        screenState.value = WelcomeScreenState.LOADING
        viewModelScope.launch {
            when (val response =
                getAthleteUseCase.getAthlete(context = context, code = accessToken)) {
                is Success -> {
                    val data = response.data
                    athlete.value = data
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
