package com.company.athleteapiart.domain.use_case.get_athlete

import android.content.Context
import com.company.athleteapiart.data.entities.AthleteEntity
import com.company.athleteapiart.data.entities.OAuth2Entity
import com.company.athleteapiart.data.remote.AthleteApi
import com.company.athleteapiart.util.Resource
import java.util.*
import javax.inject.Inject

class GetAthleteUseCase @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    suspend fun getAthlete(
        context: Context,
        code: String
    ) = getAthleteFromAuthorizationCode(code = code)


    private suspend fun getAthleteFromAuthorizationCode(
        code: String
    ): Resource<AthleteEntity> {
        val data = try {
            println("here success")
            api.getAuthenticatedAthlete(authHeader = "Bearer $code")
        } catch (e: Exception) {
            println("Here failure ${e.message}")
            return Resource.Error("An error occurred retrieving authenticated athlete. ${e.message}")
        }

        return Resource.Success(
            AthleteEntity(
                athleteId = data.id,
                userName = data.username,
                receivedOn = (GregorianCalendar().timeInMillis / 1000).toInt(),
                firstName = data.firstname,
                profilePictureMedium = data.profile_medium,
                profilePictureLarge = data.profile,
                datePreference = data.date_preference,
                lastName = data.lastname
            )
        )
    }
}