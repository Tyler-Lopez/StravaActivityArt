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
                id,
                userName,
                resourceState,
                firstName,
                lastName,
                bio,
                city,
                state,
                country,
                sex,
                premium,
                summit,
                createdAt,
                updatedAt,
                badgeTypeId,
                weight,
                profileMedium,
                profile,
                friend,
                follower,
                lastCachedYearMonth
            )
        }
        athleteDatabase.athleteDao.insertAthlete(entity)
    }
}