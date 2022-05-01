package com.company.athleteapiart.presentation.login_screen

sealed class LoginScreenState {
    object Launch : LoginScreenState()
    object Loading : LoginScreenState()
    object Standby : LoginScreenState()
    object Authorized : LoginScreenState()
}
