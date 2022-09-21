package com.company.activityart.presentation.login_screen

import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.ConnectWithStrava
import com.company.activityart.presentation.login_screen.LoginScreenViewEvent.ConnectWithStravaClicked
import com.company.activityart.presentation.login_screen.LoginScreenViewState.Standby
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        viewModelScope.launch(Dispatchers.Default) {
            when (event) {
                is ConnectWithStravaClicked -> onConnectWithStravaClicked()
            }
        }
    }

    private suspend fun onConnectWithStravaClicked() {
        routeTo(ConnectWithStrava)
    }
}