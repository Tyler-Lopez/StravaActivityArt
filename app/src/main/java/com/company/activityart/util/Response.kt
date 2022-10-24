package com.company.activityart.util

/**
 * Wrapper interface around a [TypeOfData] which is used when that data is retrieved
 * from a repository which may either be successful, [Success], or error, [Error].
 */
sealed interface Response<TypeOfData> {
    val data: TypeOfData?

    data class Success<TypeOfData>(override val data: TypeOfData) : Response<TypeOfData>
    data class Error<TypeOfData>(
        override val data: TypeOfData? = null,
        val exception: Exception? = null,
        val message: String? = null
    ) : Response<TypeOfData>

}

/**
 * Callback invoked if this [Response] is an instance of [Error].
 * @return The calling [Response].
 */
inline fun <TypeOfData> Response<TypeOfData>.doOnError(
    callback: Response.Error<TypeOfData>.() -> Unit
): Response<TypeOfData> {
    if (this is Response.Error) callback(this)
    return this
}

/**
 * Callback invoked if this [Response] is an instance of [Success].
 * @return The calling [Response].
 */
inline fun <TypeOfData> Response<TypeOfData>.doOnSuccess(
    callback: Response.Success<TypeOfData>.() -> Unit
): Response<TypeOfData> {
    if (this is Response.Success) callback(this)
    return this
}