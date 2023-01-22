package com.activityartapp.domain.useCase.authentication

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.domain.models.OAuth2
import javax.inject.Inject

/** Retrieves the POTENTIALLY-EXPIRED [OAuth2] from on-disk storage
 * or null if one does not exist. **/
class GetAccessTokenFromDisk @Inject constructor(private val athleteDatabase: AthleteDatabase) {
    suspend operator fun invoke(): OAuth2? {
        return athleteDatabase
            .oAuth2Dao
            .getCurrAuth()
    }
}