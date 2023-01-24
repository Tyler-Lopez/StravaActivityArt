package com.activityartapp.domain.useCase.activities

import com.activityartapp.data.remote.AthleteApi
import com.activityartapp.domain.models.Activity
import com.activityartapp.util.Response
import com.google.maps.android.PolyUtil
import java.util.concurrent.CancellationException
import javax.inject.Inject

/** Retrieves a [List] of [Activity] by Strava's API which occurs within a window of time
 * and is on a certain page of specified pagination.
 *
 * Intended for internal abstracted use within [GetActivitiesByYearFromRemote].*/
class GetActivitiesByPageFromRemote @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    suspend operator fun invoke(
        code: String,
        page: Int,
        activitiesPerPage: Int,
        beforeUnixSeconds: Int,
        afterUnixSeconds: Int
    ): Response<List<Activity>> {
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