package com.company.athleteapiart.domain.use_case.get_gear

import com.company.athleteapiart.data.remote.AthleteApi
import com.company.athleteapiart.data.remote.responses.DetailedGear
import com.company.athleteapiart.util.HTTPFault
import com.company.athleteapiart.util.Resource
import javax.inject.Inject

class GetGearFromApiUseCase @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    suspend fun getGearFromApi(
        gearId: String,
        accessToken: String
    ): Resource<DetailedGear> {
        return try {
            val response = api.getGearById(
                id = gearId,
                authHeader = "Bearer $accessToken"
            )
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(HTTPFault.UNKNOWN)
        }
    }
}