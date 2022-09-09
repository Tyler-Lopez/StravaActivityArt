package com.company.activityart.domain.use_case.athlete

import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.data.entities.AthleteEntity
import com.company.activityart.domain.models.Athlete
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class InsertAthleteUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(athlete: Athlete) {
        val entity = athlete.run {
            AthleteEntity(
                athleteId,
                userName,
                receivedOnUnixSeconds ?: TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
                    .toInt(),
                profilePictureMedium,
                profilePictureLarge,
                firstName,
                lastName,
                lastCachedYearMonth
            )
        }
        athleteDatabase.athleteDao.insertAthlete(entity)
    }
}