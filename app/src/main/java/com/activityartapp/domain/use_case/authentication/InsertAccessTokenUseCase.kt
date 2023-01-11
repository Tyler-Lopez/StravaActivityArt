package com.activityartapp.domain.use_case.authentication

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.data.entities.OAuth2Entity
import com.activityartapp.domain.models.OAuth2
import javax.inject.Inject

class InsertAccessTokenUseCase @Inject constructor(
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