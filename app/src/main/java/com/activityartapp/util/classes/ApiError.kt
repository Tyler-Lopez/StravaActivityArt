package com.activityartapp.util.classes

import androidx.annotation.StringRes
import com.activityartapp.util.enums.ErrorType
import com.activityartapp.util.errors.AthleteRateLimited
import retrofit2.HttpException
import java.net.UnknownHostException

sealed interface ApiError {
    /** Unauthorized error will be handled by redirecting user to the login screen **/
    object Unauthorized : ApiError

    /** A UserFacingError will be handled by informing the user why they are experiencing the error **/
    sealed interface UserFacingError : ApiError {
        object AthleteRateLimited : UserFacingError
        object NoInternet : UserFacingError
        object StravaRateLimited : UserFacingError
        object StravaServerIssues : UserFacingError
        object Unknown : UserFacingError
    }

    companion object {
        fun valueOf(value: Exception?): ApiError {
            return when (value) {
                is UnknownHostException -> UserFacingError.NoInternet
                is AthleteRateLimited -> UserFacingError.AthleteRateLimited
                is HttpException -> { when (value.code()) {
                    401, 403, 404 -> Unauthorized
                    429 -> UserFacingError.StravaRateLimited
                    500 -> UserFacingError.StravaServerIssues
                    else -> UserFacingError.Unknown
                }}
                else -> UserFacingError.Unknown
            }
        }
    }
}