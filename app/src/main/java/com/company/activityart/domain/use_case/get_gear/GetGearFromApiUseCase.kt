package com.company.activityart.domain.use_case.get_gear

import com.company.activityart.data.remote.AthleteApi
import javax.inject.Inject

class GetGearFromApiUseCase @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    /*
    suspend fun getGearFromApi(
        gearId: String,
        accessToken: String
    ): Response<DetailedGear> {
        return try {
            val response = api.getGearById(
                id = gearId,
                authHeader = "Bearer $accessToken"
            )
            Response.Success(response)
        } catch (e: Exception) {
            Response.Failure(HTTPFault.UNKNOWN)
        }
    }

     */
}