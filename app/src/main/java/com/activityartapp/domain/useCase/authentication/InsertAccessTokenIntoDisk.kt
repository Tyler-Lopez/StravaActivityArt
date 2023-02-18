package com.activityartapp.domain.useCase.authentication

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.data.entities.OAuth2Entity
import com.activityartapp.domain.models.Athlete
import com.activityartapp.domain.models.OAuth2
import javax.inject.Inject

/** Inserts an [Athlete] into on-disk storage. **/
class InsertAccessTokenIntoDisk @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(auth: Athlete) {
        val entity = auth.run {
            OAuth2Entity(
                athleteId = athleteId,
                lastCachedUnixMs = null,
                expiresAtUnixSeconds = auth.expiresAtUnixSeconds,
                accessToken = auth.accessToken,
                refreshToken = auth.refreshToken
            )
        }
        athleteDatabase.oAuth2Dao.insertOauth2(entity)
    }
}