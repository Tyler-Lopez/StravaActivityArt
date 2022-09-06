package com.company.activityart.presentation.login_screen

import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.ConnectWithStrava
import com.company.activityart.presentation.login_screen.LoginScreenViewEvent.ConnectWithStravaClicked
import com.company.activityart.presentation.login_screen.LoginScreenViewState.Standby
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor() : BaseRoutingViewModel<
        LoginScreenViewState,
        LoginScreenViewEvent,
        MainDestination
        >() {

    init {
        pushState(Standby)
    }

    override fun onEvent(event: LoginScreenViewEvent) {
        when (event) {
            is ConnectWithStravaClicked -> onConnectWithStravaClicked()
        }
    }

    private fun onConnectWithStravaClicked() {
        routeTo(ConnectWithStrava)
    }

    override fun onRouterAttached() {} // No-op
}