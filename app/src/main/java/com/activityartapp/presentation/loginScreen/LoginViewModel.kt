package com.activityartapp.presentation.loginScreen

import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.MainDestination.ConnectWithStrava
import com.activityartapp.presentation.loginScreen.LoginViewEvent.ConnectWithStravaClicked
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