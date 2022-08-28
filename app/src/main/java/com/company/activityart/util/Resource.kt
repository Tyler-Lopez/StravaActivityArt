package com.company.activityart.util

import retrofit2.HttpException

// https://youtu.be/aaChg9aJDW4?t=881
sealed class Resource<T>(open val data: T? = null) {
    data class Success<T>(override val data: T) : Resource<T>(data)
    data class Error<T>(
        override val data: T? = null,
        val exception: Exception
    ) : Resource<T>(data)

}