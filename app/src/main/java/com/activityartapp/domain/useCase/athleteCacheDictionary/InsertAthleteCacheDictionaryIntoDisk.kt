package com.activityartapp.domain.useCase.athleteCacheDictionary

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.data.entities.AthleteCacheDictionaryEntity
import com.activityartapp.domain.models.AthleteCacheDictionary
import javax.inject.Inject

class InsertAthleteCacheDictionaryIntoDisk @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(athlete: AthleteCacheDictionary) {
        athleteDatabase.athleteCacheDictionaryDao.insertAthlete(athlete.run {
            AthleteCacheDictionaryEntity(
                id = id,
                lastCachedYearMonth = lastCachedYearMonth
            )
        })
    }
}