package com.company.activityart.domain.use_case.authentication

import com.company.activityart.data.database.AthleteDatabase
import javax.inject.Inject


class ClearAccessTokenUseCase @Inject constructor(
    private val athleteDatabase: AthleteDatabase
) {
    suspend operator fun invoke() {
        return athleteDatabase
            .oAuth2Dao
            .clearOauth2()
    }
}