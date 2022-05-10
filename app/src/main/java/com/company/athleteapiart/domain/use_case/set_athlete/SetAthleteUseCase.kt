package com.company.athleteapiart.domain.use_case.set_athlete

import android.content.Context
import com.company.athleteapiart.data.database.AthleteDatabase
import com.company.athleteapiart.data.entities.AthleteEntity

class SetAthleteUseCase {
    // Invoked to set access token in Room database
    suspend fun setAthlete(
        context: Context,
        athleteEntity: AthleteEntity
    ) {
        val athleteDao = AthleteDatabase
            .getInstance(context.applicationContext)
            .athleteDao

        athleteDao.insertAthlete(athleteEntity = athleteEntity)
    }

    suspend fun updateAthleteYearlySummary(
        context: Context,
        athleteId: Long,
        activtyCount: Int
    ) {

    }
}