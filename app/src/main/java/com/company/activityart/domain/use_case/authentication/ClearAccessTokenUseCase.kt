package com.company.activityart.domain.use_case.authentication

import com.company.activityart.data.cache.ActivitiesCache
import com.company.activityart.data.database.AthleteDatabase
import javax.inject.Inject


class ClearAccessTokenUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase,
    private val cache: ActivitiesCache
) {
    suspend operator fun invoke() {
        /** Singleton cache is first cleared so that it will be fetched
         * again if a new athlete signs in */
        cache.cachedActivitiesByYear.clear()
        /** Clear ROOM storage entry containing current authentication **/
        return athleteDatabase
            .oAuth2Dao
            .clearOauth2()
    }
}