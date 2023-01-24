package com.activityartapp.domain.useCase.authentication

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.data.entities.OAuth2Entity
import com.activityartapp.domain.models.OAuth2
import javax.inject.Inject

/** Inserts an [OAuth2] into on-disk storage. **/
class InsertAccessTokenIntoDisk @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(auth: OAuth2) {
        val entity = auth.run {
            OAuth2Entity(
                athleteId,
                expiresAtUnixSeconds,
                accessToken,
                refreshToken
            )
        }
        athleteDatabase.oAuth2Dao.insertOauth2(entity)
    }
}