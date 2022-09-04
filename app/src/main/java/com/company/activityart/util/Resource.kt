package com.company.activityart.util

// https://youtu.be/aaChg9aJDW4?t=881
sealed class Resource<TypeOfData>(open val data: TypeOfData? = null) {
    data class Success<TypeOfData>(override val data: TypeOfData) : Resource<TypeOfData>(data)
    data class Error<TypeOfData>(
        override val data: TypeOfData? = null,
        val exception: Exception? = null,
        val message: String? = null
    ) : Resource<TypeOfData>(data)

    fun doOnSuccess(callback: Success<TypeOfData>.() -> Unit): Resource<TypeOfData> {
        if (this is Success)  callback(this)
        return this
    }

    fun doOnError(callback: Error<TypeOfData>.() -> Unit): Resource<TypeOfData> {
        if (this is Error) callback(this)
        return this
    }
}