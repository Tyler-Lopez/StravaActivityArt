package com.company.athleteapiart.domain.use_case

import com.company.athleteapiart.domain.use_case.get_gear.GetGearFromApiUseCase
import com.company.athleteapiart.domain.use_case.get_gear.GetGearsFromRoomUseCase

data class GearUseCases(
    val getGearsFromRoomUseCase: GetGearsFromRoomUseCase,
    val getGearFromApiUseCase: GetGearFromApiUseCase,
)