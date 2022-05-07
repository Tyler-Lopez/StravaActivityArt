package com.company.athleteapiart.domain.use_case

import com.company.athleteapiart.domain.use_case.get_athlete.GetAthleteUseCase
import com.company.athleteapiart.domain.use_case.set_athlete.SetAthleteUseCase

data class AthleteUseCases(
    val getAthleteUseCase: GetAthleteUseCase,
    val setAthleteUseCase: SetAthleteUseCase
)