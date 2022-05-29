package com.company.activityart.domain.use_case.get_activities

import android.content.Context
import com.company.activityart.data.database.ActivityDatabase
import com.company.activityart.data.entities.ActivityEntity
import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.util.HTTPFault
import com.company.activityart.util.Resource
import com.company.activityart.util.TimeUtils
import java.util.*
import javax.inject.Inject

class GetActivitiesUseCase @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    suspend fun getActivitiesByYearFromCache(
        context: Context,
        athleteId: Long,
        year: Int
    ): List<ActivityEntity> = ActivityDatabase
        .getInstance(context.applicationContext)
        .activityDao
        .getActivitiesByYear(
            athleteId = athleteId,
            year = year
        )

    suspend fun getActivitiesByYearMonthFromCache(
        context: Context,
        athleteId: Long,
        year: Int,
        month: Int
    ): List<ActivityEntity> = ActivityDatabase
        .getInstance(context.applicationContext)
        .activityDao
        .getActivitiesByYearMonth(
            athleteId = athleteId,
            year = year,
            month = month
        )

    suspend fun getActivitiesByYearFromRoom(
        context: Context,
        athleteId: Long,
        year: Int,
        lastCachedMonth: Int
    ): List<ActivityEntity> = ActivityDatabase
        .getInstance(context.applicationContext)
        .activityDao
        .getActivitiesByYearUpToMonth(
            athleteId = athleteId,
            month = lastCachedMonth, // Room takes 1-12, lastCachedMonth comes in 0-11
            year = year
        )

    suspend fun getActivitiesAfterMonthInYearFromApi(
        accessToken: String,
        year: Int,
        afterMonth: Int
    ): Resource<List<ActivityEntity>> {

        println("After month was input as $afterMonth for year $year")
        /* READING FROM API */
        val getBefore: Int =
            ((GregorianCalendar(year + 1, 0, 0)
                .timeInMillis) / 1000).toInt()
        val getAfter: Int =
            ((GregorianCalendar(year, maxOf(afterMonth, 0), 0, 23, 59, 59)
                .timeInMillis) / 1000).toInt()

        println("Get before is $getBefore get after is $getAfter")
        val yearlyActivities = mutableListOf<ActivityEntity>()
        try {
            var page = 1
            do {
                val activitiesResponse = getActivities(
                    accessToken = accessToken,
                    page = page++,
                    before = getBefore,
                    after = getAfter
                )
                for (activity in activitiesResponse) {
                    val activityDateMap = TimeUtils.parseIso8601(activity.start_date_local)
                    yearlyActivities.add(
                        ActivityEntity(
                            activityId = activity.id,
                            athleteId = activity.athlete.id,
                            activityName = activity.name,
                            activityType = activity.type,
                            activityMonth = activityDateMap["month"]!!,
                            activityYear = activityDateMap["year"]!!,
                            activityDate = activity.start_date_local,
                            activityDistance = activity.distance,
                            summaryPolyline = activity.map.summary_polyline,
                            locationCity = activity.location_city,
                            locationState = activity.location_state,
                            locationCountry = activity.location_country,
                            maxSpeed = activity.max_speed,
                            averageSpeed = activity.average_speed,
                            movingTime = activity.moving_time,
                            gearId = activity.gear_id,
                            kudosCount = activity.kudos_count
                        )
                    )
                }
            } while (activitiesResponse.isNotEmpty())
        } catch (e: Exception) {
            println("An error has occurred - ${e.message}")
            return Resource.Error(HTTPFault.getEnum(e.message))
        }
        return Resource.Success(yearlyActivities)
    }
    // Keep invoking this until it returns an empty list and incrementing page count
    private suspend fun getActivities(
        accessToken: String,
        page: Int,
        before: Int,
        after: Int
    ) = api.getActivities(
        authHeader = "Bearer $accessToken",
        page = page,
        perPage = 200,
        before = before,
        after = after
    )


}