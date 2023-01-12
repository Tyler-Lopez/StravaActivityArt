package com.activityartapp.domain.use_case.athlete

import com.activityartapp.data.remote.AthleteApi
import com.activityartapp.activityart.domain.models.Athlete
import com.activityartapp.util.Response
import com.activityartapp.util.Response.Error
import com.activityartapp.util.Response.Success
import java.util.concurrent.CancellationException
import javax.inject.Inject

class GetAthleteFromRemoteUseCase @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    suspend operator fun invoke(code: String): Response<Athlete> {
        return try {
            Success(data = api.getAuthenticatedAthlete(authHeader = "Bearer $code"))
        } catch (e: Exception) {
            /* When using try catch in a suspend block,
            ensure we do not catch CancellationException */
            if (e is CancellationException) throw e
            e.printStackTrace()
            Error(exception = e)
        }
    }
}