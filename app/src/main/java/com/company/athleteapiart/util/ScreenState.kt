package com.company.athleteapiart.util

import com.company.athleteapiart.Screen

enum class ScreenState {
    LAUNCH, LOADING, STANDBY
}

enum class ApiScreenState {
    LAUNCH, LOADING, STANDBY, ERROR, DISCONNECTED
}

enum class AuthScreenState {
    LAUNCH, LOADING, STANDBY, ERROR, AUTHORIZED
}