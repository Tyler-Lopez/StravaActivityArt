package com.activityartapp.presentation.welcomeScreen

import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.domain.useCase.authentication.ClearAthleteFromDisk
import com.activityartapp.domain.useCase.version.GetVersionFromRemote
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.MainDestination.*
import com.activityartapp.presentation.errorScreen.ErrorScreenType
import com.activityartapp.presentation.welcomeScreen.WelcomeViewEvent.*
import com.activityartapp.util.doOnError
import com.activityartapp.util.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val clearAthleteFromDisk: ClearAthleteFromDisk,
    private val getVersionFromRemote: GetVersionFromRemote
) :
    BaseRoutingViewModel<WelcomeViewState, WelcomeViewEvent, MainDestination>() {

    init {
        WelcomeViewState.Loading.push()
        viewModelScope.launch(Dispatchers.IO) {
            getVersionFromRemote()
                .doOnSuccess {
                    if (!data.isSupported) {
                        routeTo(
                            NavigateError(
                                clearNavigationHistory = true,
                                errorScreenType = ErrorScreenType.UNSUPPORTED_VERSION
                            )
                        )
                    } else {
                        WelcomeViewState.Standby(
                            data.isLatest
                        ).push()
                    }
                }
                .doOnError {
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
            clearAthleteFromDisk()
            routeTo(NavigateLogin)
        }
    }
}