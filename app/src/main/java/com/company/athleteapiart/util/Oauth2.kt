package com.company.athleteapiart.util

import java.util.*

object Oauth2 {
    var authorizationCode: String = "null"
    var accessToken: String = "null"
    var refreshToken: String = "null"
    private var accessTokenReceivedOn: Int = -1

    fun accessTokenReceived(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        accessTokenReceivedOn = (GregorianCalendar().timeInMillis / 1000).toInt()
    }

}