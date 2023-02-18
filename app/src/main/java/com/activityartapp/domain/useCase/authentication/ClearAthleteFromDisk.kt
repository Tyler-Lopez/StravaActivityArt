package com.activityartapp.domain.useCase.authentication

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.models.Athlete
import javax.inject.Inject

/** Clears all access tokens [Athlete] which exist in on-disk storage
 *  and any [Activity] which exist in memory.**/
class ClearAthleteFromDisk @Inject constructor(
    private val athleteDatabase: AthleteDatabase,
) {
    suspend operator fun invoke() {
        /** Clear ROOM storage entry containing current authentication **/
        return athleteDatabase
            .athleteDao
            .clearAthlete()
    }
}