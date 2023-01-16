package com.activityartapp.presentation.welcomeScreen

import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.domain.use_case.authentication.ClearAccessTokenUseCase
import com.activityartapp.domain.use_case.version.GetVersion
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.MainDestination.*
import com.activityartapp.presentation.welcomeScreen.WelcomeViewEvent.*
import com.activityartapp.util.doOnError
import com.activityartapp.util.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val clearAccessTokenUseCase: ClearAccessTokenUseCase,
    private val getVersion: GetVersion
) :
    BaseRoutingViewModel<WelcomeViewState, WelcomeViewEvent, MainDestination>() {

    init {
        WelcomeViewState.Loading.push()
        viewModelScope.launch(Dispatchers.IO) {
            getVersion()
                .doOnSuccess {
                    println("Version received ${data.isLatest} ${data.isSupported}")
                    if (!data.isSupported) {
                        routeTo(NavigateUnsupportedVersion)
                    } else {
                        WelcomeViewState.Standby(
                            data.isLatest
                        ).push()
                    }
                }
                .doOnError {
                    println("Error was $exception")
                    WelcomeViewState.Standby(
                        versionIsLatest = true
                    ).push()
                }
        }
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