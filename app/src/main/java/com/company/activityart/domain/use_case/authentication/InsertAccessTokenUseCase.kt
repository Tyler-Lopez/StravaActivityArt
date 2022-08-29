package com.company.activityart.domain.use_case.authentication

import android.content.Context
import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.data.entities.AthleteEntity
import com.company.activityart.data.entities.OAuth2Entity
import com.company.activityart.domain.models.OAuth2
import javax.inject.Inject
import kotlin.math.exp

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