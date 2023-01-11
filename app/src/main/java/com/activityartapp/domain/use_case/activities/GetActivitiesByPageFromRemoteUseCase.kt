package com.activityartapp.domain.use_case.activities

import com.activityartapp.data.remote.AthleteApi
import com.activityartapp.domain.models.Activity
import com.activityartapp.activityart.util.Response
import com.google.maps.android.PolyUtil
import java.util.concurrent.CancellationException
import javax.inject.Inject

/**
 * Retrieves a [List] of [Activity] from remote repository occurring within a
 * given year by a certain page from paginated results.
 *
 * Intended for internal abstracted use within [GetActivitiesByYearFromRemoteUseCase].
 */
class GetActivitiesByPageFromRemoteUseCase @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    suspend operator fun invoke(
        code: String,
        page: Int,
        activitiesPerPage: Int,
        beforeUnixSeconds: Int,
        afterUnixSeconds: Int
    ): Response<List<Activity>> {
        println("here before is $beforeUnixSeconds")
        println("here after is $afterUnixSeconds")
        return try {
            Response.Success(data = api.getActivities(
                authHeader = "Bearer $code",
                page = page,
                perPage = activitiesPerPage,
                before = beforeUnixSeconds,
                after = afterUnixSeconds
            )
                .toList()
                .filter {
                    it.summaryPolyline?.run {
                        PolyUtil.decode(this).isNotEmpty()
                    } == true
                })
        } catch (e: Exception) {
            /* When using try catch in a suspend block,
            ensure we do not catch CancellationException */
            if (e is CancellationException) throw e
            e.printStackTrace()
            Response.Error(exception = e)
        }
    }
}