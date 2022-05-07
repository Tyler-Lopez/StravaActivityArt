package com.company.athleteapiart.domain.use_case.get_athlete

import com.company.athleteapiart.data.remote.AthleteApi
import javax.inject.Inject

class GetAthleteUseCase @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
}