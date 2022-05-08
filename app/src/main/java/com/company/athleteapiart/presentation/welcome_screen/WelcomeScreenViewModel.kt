package com.company.athleteapiart.presentation.welcome_screen

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.domain.use_case.AthleteUseCases
import com.company.athleteapiart.domain.use_case.AuthenticationUseCases
import com.company.athleteapiart.domain.use_case.clear_access_token.ClearAccessTokenUseCase
import com.company.athleteapiart.presentation.login_screen.LoginScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val athleteUseCases: AthleteUseCases,
    private val authenticationUseCases: AuthenticationUseCases
) : ViewModel() {

    // State - observed in the view
    val screenState = mutableStateOf(WelcomeScreenState.LAUNCH)

    fun getAthlete(
        athleteId: Int,
        accessToken: String
    ) {
        screenState.value = WelcomeScreenState.LOADING
        viewModelScope.launch {
            delay(1000) // Placeholder
            screenState.value = WelcomeScreenState.STANDBY
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
