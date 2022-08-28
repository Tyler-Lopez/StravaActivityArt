package com.company.activityart.domain.use_case.athlete

import android.content.Context
import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.data.entities.AthleteEntity
import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.domain.models.Athlete
import javax.inject.Inject

class InsertAthleteUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(athlete: Athlete) {
        val entity = athlete.run {
            AthleteEntity(
                athleteId,
                userName,
                receivedOnUnixSeconds,
                profilePictureMedium,
                profilePictureLarge,
                firstName,
                lastName,
                yearMonthsCached,
                gears
            )
        }
        athleteDatabase.athleteDao.insertAthlete(entity)
    }
}