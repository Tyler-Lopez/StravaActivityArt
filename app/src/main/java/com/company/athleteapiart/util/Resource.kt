package com.company.athleteapiart.util

// https://youtu.be/aaChg9aJDW4?t=881
sealed class Resource<T> (open val data: T? = null, open val message: String? = null) {
    class Success<T> (override val data: T) : Resource<T> (data)
    class Error<T> (override val message: String, data: T? = null) : Resource<T> (data, message)
}