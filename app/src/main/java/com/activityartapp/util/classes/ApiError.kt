package com.activityartapp.util.classes

import com.activityartapp.util.errors.AthleteRateLimitedException
import retrofit2.HttpException
import java.net.UnknownHostException

sealed interface ApiError {
    object Unauthorized : ApiError
    object AthleteRateLimited : ApiError
    object NoInternet : ApiError
    object StravaRateLimited : ApiError
    object StravaServerIssues : ApiError
    object Unknown : ApiError

    companion object {
        fun valueOf(value: Exception?): ApiError {
            println("Determining what exception $value is")
            return when (value) {
                is UnknownHostException -> NoInternet
                is AthleteRateLimitedException -> AthleteRateLimited
                is HttpException -> {
                    when (value.code()) {
                        401, 403, 404 -> Unauthorized
                        429 -> StravaRateLimited
                        500 -> StravaServerIssues
                        else -> Unknown
                    }
                }
                else -> Unknown
            }
        }
    }
}