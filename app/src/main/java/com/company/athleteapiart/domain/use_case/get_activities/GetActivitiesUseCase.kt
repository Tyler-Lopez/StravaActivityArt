package com.company.athleteapiart.domain.use_case.get_activities

import android.content.Context
import com.company.athleteapiart.data.database.ActivityDatabase
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.data.entities.AthleteEntity
import com.company.athleteapiart.data.remote.AthleteApi
import com.company.athleteapiart.data.remote.responses.Activities
import com.company.athleteapiart.util.HTTPFault
import com.company.athleteapiart.util.Resource
import com.company.athleteapiart.util.TimeUtils
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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

    // This is invoked to get all activities from ROOM AND API for a given a year
    suspend fun getActivitiesByYear(
        context: Context,
        athleteEntity: AthleteEntity,
        accessToken: String,
        year: Int
    ): Resource<List<ActivityEntity>> {

        // First, determine the last cached month of this year
        // This is so we can know which months we need to use API to retrieve
        // Equal to -1 if never cached, 11 if completely cached
        println("here, year is $year")
        val lastCachedMonth = athleteEntity.lastCachedMonth(year = year)

        val yearlyActivities = mutableListOf<ActivityEntity>()

        /* READING FROM ROOM */
        yearlyActivities.addAll(
            getActivitiesByYearFromRoom(
                context = context,
                athleteId = athleteEntity.athleteId,
                year = year,
                lastCachedMonth = lastCachedMonth
            )
        )

        /* READING FROM API */
        val getBefore: Int =
            ((GregorianCalendar(year + 1, 0, 0)
                .timeInMillis) / 1000).toInt()
        val getAfter: Int =
            ((GregorianCalendar(year, lastCachedMonth + 1, 0)
                .timeInMillis) / 1000).toInt()

        if (lastCachedMonth != 11)
            try {
                println("last cached month is $lastCachedMonth")
                println("Here, last cached month not equal to 11")
                var page = 1
                do {
                    val activitiesResponse = getActivities(
                        accessToken = accessToken,
                        page = page++,
                        before = getBefore,
                        after = getAfter
                    )
                    println("here after response")
                    val activities = mutableListOf<ActivityEntity>()
                    for (activity in activitiesResponse) {
                        val activityDateMap = TimeUtils.parseIso8601(activity.start_date_local)
                        activities.add(
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
                    yearlyActivities.addAll(activities)
                } while (activitiesResponse.isNotEmpty())
            } catch (e: Exception) {
                println("An error has occurred - ${e.message}")
                return Resource.Error(HTTPFault.getEnum(e.message))
            }
        // Return all activities we've retrieved and await all of the deferred results from ROOM
        return Resource.Success(data = yearlyActivities)

    }

    private suspend fun getActivitiesByYearFromRoom(
        context: Context,
        athleteId: Long,
        year: Int,
        lastCachedMonth: Int
    ): List<ActivityEntity> = ActivityDatabase
        .getInstance(context.applicationContext)
        .activityDao
        .getActivitiesByYearUpToMonth(
            athleteId = athleteId,
            month = lastCachedMonth,
            year = year
        )

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