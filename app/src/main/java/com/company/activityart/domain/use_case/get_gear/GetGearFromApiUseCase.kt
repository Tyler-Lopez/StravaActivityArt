package com.company.activityart.domain.use_case.get_gear

import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.data.remote.responses.DetailedGear
import com.company.activityart.util.Resource
import javax.inject.Inject

class GetGearFromApiUseCase @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    /*
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
            Resource.Failure(HTTPFault.UNKNOWN)
        }
    }

     */
}