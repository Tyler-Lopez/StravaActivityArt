package com.company.activityart.domain.use_case.activities

import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.domain.models.Activity
import com.company.activityart.util.Resource
import java.util.concurrent.CancellationException
import javax.inject.Inject

class GetActivitiesByYearByPageFromRemoteUseCase @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    suspend operator fun invoke(
        code: String,
        page: Int,
        activitiesPerPage: Int,
        beforeUnixSeconds: Int,
        afterUnixSeconds: Int
    ): Resource<List<Activity>> {
        return try {
            Resource.Success(data = api.getActivities(
                authHeader = "Bearer $code",
                page = page,
                perPage = activitiesPerPage,
                before = beforeUnixSeconds,
                after = afterUnixSeconds
            ).toList())
        } catch (e: Exception) {
            /* When using try catch in a suspend block,
            ensure we do not catch CancellationException */
            if (e is CancellationException) throw e
            e.printStackTrace()
            Resource.Error(exception = e)
        }
    }
}