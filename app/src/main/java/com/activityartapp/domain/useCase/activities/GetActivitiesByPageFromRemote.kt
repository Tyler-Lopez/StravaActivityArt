package com.activityartapp.domain.useCase.activities

import com.activityartapp.data.remote.AthleteApi
import com.activityartapp.domain.models.Activity
import com.activityartapp.util.Response
import com.google.maps.android.PolyUtil
import java.util.concurrent.CancellationException
import javax.inject.Inject

/** Retrieves a [List] of [Activity] by Strava's API by page without specifying before and
 * after epoch timestamp parameters.
 *
 * Intended for internal abstracted use within [GetActivitiesByYearFromRemote].*/
class GetActivitiesByPageFromRemote @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    suspend operator fun invoke(
        code: String,
        page: Int,
        activitiesPerPage: Int
    ): Response<List<Activity>> {
        return try {
            Response.Success(data = api.getActivities(
                authHeader = "Bearer $code",
                page = page,
                perPage = activitiesPerPage
            )
                .toList()
                .filter {
                    it.summaryPolyline?.run {
                        PolyUtil.decode(this).isNotEmpty()
                    } == true
                })
        } catch (e: Exception) {
            println("Exception in by page for page $page and code was $code")
            /* When using try catch in a suspend block,
            ensure we do not catch CancellationException */
            if (e is CancellationException) throw e
            e.printStackTrace()
            Response.Error(exception = e)
        }
    }
}