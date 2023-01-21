package com.activityartapp.util.enums

import retrofit2.HttpException
import java.lang.Exception
import java.net.UnknownHostException

enum class ErrorType {
    ATHLETE_RATE_LIMITED,
    APP_RATE_LIMITED,
    NO_INTERNET,
    UNKNOWN,
    UNAUTHORIZED,
    STRAVA_SERVER_ISSUES;

    companion object {
        fun valueOf(exception: Exception?): ErrorType {
            return when (exception) {
                is UnknownHostException -> NO_INTERNET
                is HttpException -> when (exception.response()?.code()) {
                    401, 403, 404 -> UNAUTHORIZED
                    429 -> APP_RATE_LIMITED
                    500 -> STRAVA_SERVER_ISSUES
                    else -> UNKNOWN
                }
                else -> UNKNOWN
            }
        }
    }
}