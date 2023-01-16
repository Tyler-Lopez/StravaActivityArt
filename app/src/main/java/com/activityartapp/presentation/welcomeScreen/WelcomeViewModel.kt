package com.activityartapp.presentation.welcomeScreen

import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.activityart.domain.models.Athlete
import com.activityartapp.domain.models.OAuth2
import com.activityartapp.domain.models.fullName
import com.activityartapp.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.activityartapp.domain.use_case.athlete.GetAthleteUseCase
import com.activityartapp.domain.use_case.athleteUsage.GetAthleteUsage
import com.activityartapp.domain.use_case.athleteUsage.IncrementAthleteUsage
import com.activityartapp.domain.use_case.authentication.ClearAccessTokenUseCase
import com.activityartapp.domain.use_case.authentication.GetAccessTokenUseCase
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.MainDestination.*
import com.activityartapp.presentation.welcomeScreen.WelcomeViewEvent.*
import com.activityartapp.util.Response
import com.activityartapp.util.classes.ApiError
import com.activityartapp.util.doOnError
import com.activityartapp.util.doOnSuccess
import com.activityartapp.util.enums.ErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class WelcomeViewModel @Inject constructor(private val clearAccessTokenUseCase: ClearAccessTokenUseCase) :
    BaseRoutingViewModel<WelcomeViewState, WelcomeViewEvent, MainDestination>() {

    init {
        WelcomeViewState.push()
    }

    override fun onEvent(event: WelcomeViewEvent) {
        when (event) {
            is ClickedAbout -> onClickedAbout()
            is ClickedMakeArt -> onClickedMakeArt()
            is ClickedLogout -> onClickedLogout()
        }
    }

    private fun onClickedAbout() {
        viewModelScope.launch {
            routeTo(NavigateAbout)
        }
    }

    private fun onClickedMakeArt() {
        viewModelScope.launch {
            routeTo(NavigateLoadActivities)
        }
    }

    private fun onClickedLogout() {
        viewModelScope.launch {
            clearAccessTokenUseCase()
            routeTo(NavigateLogin)
        }
    }
}