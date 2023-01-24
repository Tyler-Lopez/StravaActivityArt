package com.activityartapp.domain.useCase.athleteCacheDictionary

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.domain.models.AthleteCacheDictionary
import javax.inject.Inject

class GetAthleteCacheDictionaryFromDisk @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(athleteId: Long): AthleteCacheDictionary? {
        return athleteDatabase
            .athleteCacheDictionaryDao
            .getAthleteCacheDictionaryById(athleteId)
    }
}