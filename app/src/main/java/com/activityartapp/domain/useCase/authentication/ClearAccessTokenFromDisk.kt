package com.activityartapp.domain.useCase.authentication

import com.activityartapp.data.cache.ActivitiesCache
import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.models.OAuth2
import javax.inject.Inject

/** Clears all access tokens [OAuth2] which exist in on-disk storage
 *  and any [Activity] which exist in memory.**/
class ClearAccessTokenFromDisk @Inject constructor(
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