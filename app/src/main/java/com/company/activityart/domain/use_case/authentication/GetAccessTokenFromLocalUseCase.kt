package com.company.activityart.domain.use_case.authentication

import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.domain.models.OAuth2
import javax.inject.Inject

class GetAccessTokenFromLocalUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke(athleteId: Long): OAuth2? {
        return athleteDatabase
            .oAuth2Dao
            .getOauth2ByAthleteId(athleteId)
    }
}