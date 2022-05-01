package com.company.athleteapiart.presentation.login_screen

sealed class LoginScreenState {
    object Launch
    object Loading
    object Standby
    object Authorized
}
