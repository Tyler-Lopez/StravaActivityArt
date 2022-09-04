package com.company.activityart.util

// https://youtu.be/aaChg9aJDW4?t=881
sealed class Resource<TypeOfData>(open val data: TypeOfData? = null) {

    data class Success<TypeOfData>(override val data: TypeOfData) : Resource<TypeOfData>(data)
    data class Error<TypeOfData>(
        override val data: TypeOfData? = null,
        val exception: Exception? = null,
        val message: String? = null
    ) : Resource<TypeOfData>(data)

    /**
     * Callback invoked if this [Resource] is an instance of [Success].
     * @return The calling [Resource].
     */
    inline fun doOnSuccess(
        callback: Success<TypeOfData>.() -> Unit
    ): Resource<TypeOfData> {
        if (this is Success) callback(this)
        return this
    }

    /**
     * Callback invoked if this [Resource] is an instance of [Error].
     * @return The calling [Resource].
     */
    inline fun doOnError(
        callback: Error<TypeOfData>.() -> Unit
    ): Resource<TypeOfData> {
        if (this is Error) callback(this)
        return this
    }
}