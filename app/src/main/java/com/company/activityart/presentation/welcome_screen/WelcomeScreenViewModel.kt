package com.company.activityart.presentation.welcome_screen

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.activityart.data.entities.AthleteEntity
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewState.*
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
    private val _viewState: MutableState<WelcomeScreenViewState> = mutableStateOf(Launch)
    val viewState: State<WelcomeScreenViewState> = _viewState

    // Received Athlete
    private val athlete = mutableStateOf<AthleteEntity?>(null)
    private val athleteImageUrl: String
        get() = athlete.value?.profilePictureLarge ?: "via.placeholder.com/128"
    private val athleteName: String
        get() = "${athlete.value?.firstName} ${athlete.value?.lastName}"

    fun getAthlete(
        context: Context,
        athleteId: Long,
        accessToken: String
    ) {
        _viewState.value = Loading
        viewModelScope.launch {
            when (val response =
                getAthleteUseCase.getAthlete(context = context, athleteId = athleteId, code = accessToken)) {
                is Success -> {
                    val data = response.data
                    athlete.value = data
                    setAthleteUseCase.setAthlete(context = context, athleteEntity = data)
                    _viewState.value = Standby
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
        _viewState.value = Loading
        viewModelScope.launch {
            authenticationUseCases.clearAccessTokenUseCase.clearAccessToken(context = context)
            _viewState.value = Logout
        }
    }
}
