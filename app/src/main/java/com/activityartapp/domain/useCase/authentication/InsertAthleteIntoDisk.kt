package com.activityartapp.domain.useCase.authentication

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.data.entities.AthleteEntity
import com.activityartapp.domain.models.Athlete
import javax.inject.Inject

/** Inserts an [Athlete] into on-disk storage. **/
class InsertAthleteIntoDisk @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(auth: Athlete) {
        val entity = auth.run {
            AthleteEntity(
                athleteId = athleteId,
                lastCachedUnixMs = null,
                expiresAtUnixSeconds = auth.expiresAtUnixSeconds,
                accessToken = auth.accessToken,
                refreshToken = auth.refreshToken
            )
        }
        athleteDatabase.athleteDao.insertAthlete(entity)
    }
}