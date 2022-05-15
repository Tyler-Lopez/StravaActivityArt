package com.company.athleteapiart.domain.use_case.get_gear

import android.content.Context
import com.company.athleteapiart.data.database.AthleteDatabase

class GetGearsFromRoomUseCase {

    suspend fun getGearsFromRoom(
        context: Context,
        athleteId: Long
    ): Map<String, String>? =
        AthleteDatabase
            .getInstance(context)
            .athleteDao
            .getAthleteById(athleteId)
            ?.gears

}
