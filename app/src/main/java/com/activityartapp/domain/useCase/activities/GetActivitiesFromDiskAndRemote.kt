package com.activityartapp.domain.useCase.activities

import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.models.Athlete
import com.activityartapp.domain.useCase.athleteUsage.GetAthleteUsageFromRemote
import com.activityartapp.domain.useCase.athleteUsage.InsertAthleteUsageIntoRemote
import com.activityartapp.util.Response
import com.activityartapp.util.doOnError
import com.activityartapp.util.doOnSuccess
import com.activityartapp.util.errors.AthleteRateLimitedException
import java.net.UnknownHostException
import javax.inject.Inject

class GetActivitiesFromDiskAndRemote @Inject constructor(
    private val getActivitiesByPageFromRemote: GetActivitiesByPageFromRemote,
    private val getActivitiesFromDisk: GetActivitiesFromDisk,
    private val getAthleteUsageFromRemote: GetAthleteUsageFromRemote,
    private val insertAthleteUsageIntoRemote: InsertAthleteUsageIntoRemote,
    private val insertActivitiesIntoDisk: InsertActivitiesIntoDisk,
    private val insertActivitiesIntoMemory: InsertActivitiesIntoMemory
) {
    companion object {
        private const val MAXIMUM_USAGE = 25
        private const val MAXIMUM_PAGE = 10
        private const val PAGE_FIRST = 1
    }

    suspend operator fun invoke(
        athlete: Athlete,
        internetEnabled: Boolean,
        onActivitiesLoaded: (Int) -> Unit
    ): Response<List<Activity>> {
        return athlete.run {
            /** If there are cached activities, add them to activities **/
            val cachedActivities = getActivitiesFromDisk(athleteId)
            onActivitiesLoaded(cachedActivities.size)
            val cachedActivitiesIds = cachedActivities.map { it.id }.toSet()

            /** If we've reached this point and internet access is disabled, send back [UnknownHostException] **/
            if (!internetEnabled) {
                return@run Response.Error(
                    data = cachedActivities.toList(),
                    exception = UnknownHostException()
                )
            }

            /** Get the athlete's usage from remote
             * If we were unable to get it, return cached activities and an error **/
            var usage: Int
            getAthleteUsageFromRemote(athleteId)
                .run {
                    when (this) {
                        is Response.Success -> usage = data
                        is Response.Error -> return Response.Error(
                            data = cachedActivities,
                            exception = exception
                        )
                    }
                }

            println("got usage, it was $usage")

            var page = PAGE_FIRST
            var activitiesFromRemoteUnique = true
            val remoteActivities = mutableListOf<Activity>()
            while (usage < MAXIMUM_USAGE && page < MAXIMUM_PAGE && activitiesFromRemoteUnique) {
                /** Load this page of activities from remote **/
                getActivitiesByPageFromRemote(code = accessToken, page = page)
                    .doOnSuccess {
                        page++
                        insertAthleteUsageIntoRemote(athleteId, ++usage)
                        /** If there are no common activities between remote & cache, continue loading **/
                        val newActivities = data.filter {
                            !cachedActivitiesIds.contains(it.id)
                        }

                        activitiesFromRemoteUnique = newActivities.size == data.size

                        /** Add all activities **/
                        remoteActivities.addAll(newActivities)
                        onActivitiesLoaded(newActivities.size)
                    }
                    .doOnError {
                        return@run Response.Error(
                            data = cachedActivities,
                            exception = this.exception
                        )
                    }
            }

            /** Insert activities from remote into disk cache **/
            if (page == MAXIMUM_PAGE || usage < MAXIMUM_USAGE) {
                insertActivitiesIntoDisk(
                    activities = remoteActivities,
                    athleteId = athleteId
                )
            }

            val allActivities = cachedActivities.plus(remoteActivities)

            /** If the athlete has exceeded the maximum temporary usage, return an error */
            if (usage >= MAXIMUM_USAGE) {
                return@run Response.Error(
                    data = allActivities,
                    exception = AthleteRateLimitedException
                )
            }

            return@run Response.Success(data = allActivities)
        }.also { response ->
            response.data?.let { insertActivitiesIntoMemory(it) }
        }
    }
}