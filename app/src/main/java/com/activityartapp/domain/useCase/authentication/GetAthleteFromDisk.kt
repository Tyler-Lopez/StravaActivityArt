package com.activityartapp.domain.useCase.authentication

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.domain.models.Athlete
import javax.inject.Inject

/** Retrieves the POTENTIALLY-EXPIRED [Athlete] from on-disk storage
 * or null if one does not exist. **/
class GetAthleteFromDisk @Inject constructor(private val athleteDatabase: AthleteDatabase) {
    suspend operator fun invoke(): Athlete? {
        return athleteDatabase
            .athleteDao
            .getCurrAthlete()
    }
}