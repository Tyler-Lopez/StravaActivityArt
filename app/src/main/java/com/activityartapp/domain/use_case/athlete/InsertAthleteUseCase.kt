package com.activityartapp.domain.use_case.athlete

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.data.entities.AthleteEntity
import com.activityartapp.domain.models.Athlete
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class InsertAthleteUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(athlete: Athlete) {
        val entity = athlete.run {
            AthleteEntity(
                athleteId,
                // TODO COMMENTED FOR NOW      userName,
                // TODO COMMENTED FOR NOW      receivedOnUnixSeconds ?: TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
                // TODO COMMENTED FOR NOW          .toInt(),
                // TODO COMMENTED FOR NOW    profilePictureMedium,
                // TODO COMMENTED FOR NOW    profilePictureLarge,
                // TODO COMMENTED FOR NOW      firstName,
                // TODO COMMENTED FOR NOW     lastName,
                lastCachedYearMonth
            )
        }
        athleteDatabase.athleteDao.insertAthlete(entity)
    }
}