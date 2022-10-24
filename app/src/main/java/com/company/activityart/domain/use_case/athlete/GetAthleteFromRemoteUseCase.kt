package com.company.activityart.domain.use_case.athlete

import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.domain.models.Athlete
import com.company.activityart.util.Response
import com.company.activityart.util.Response.Error
import com.company.activityart.util.Response.Success
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