package com.company.activityart.presentation.login_screen

import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.ConnectWithStrava
import com.company.activityart.presentation.login_screen.LoginViewEvent.ConnectWithStravaClicked
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseRoutingViewModel<
        LoginViewState,
        LoginViewEvent,
        MainDestination
        >() {

    init {
        LoginViewState().push()
    }

    override fun onEvent(event: LoginViewEvent) {
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