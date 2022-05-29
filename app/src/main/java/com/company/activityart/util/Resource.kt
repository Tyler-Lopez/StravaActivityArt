package com.company.activityart.util

// https://youtu.be/aaChg9aJDW4?t=881
sealed class Resource<T> (open val data: T? = null, open val fault: HTTPFault? = null) {
    class Success<T> (override val data: T, override val fault: HTTPFault? = null) : Resource<T> (data)
    class Error<T> (override val fault: HTTPFault, override val data: T? = null) : Resource<T> (data, fault)
}