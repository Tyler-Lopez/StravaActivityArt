package com.activityartapp.domain.use_case.authentication

import com.activityartapp.data.database.AthleteDatabase
import com.activityartapp.domain.models.OAuth2
import javax.inject.Inject

class GetAccessTokenFromLocalUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(): OAuth2? {
        return athleteDatabase
            .oAuth2Dao
            .getCurrAuth()
    }
}