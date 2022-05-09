package com.company.athleteapiart.domain.use_case.get_athlete

import android.content.Context
import com.company.athleteapiart.data.database.AthleteDatabase
import com.company.athleteapiart.data.database.OAuth2Database
import com.company.athleteapiart.data.entities.AthleteEntity
import com.company.athleteapiart.data.entities.OAuth2Entity
import com.company.athleteapiart.data.remote.AthleteApi
import com.company.athleteapiart.util.Resource
import com.company.athleteapiart.util.clientSecret
import java.util.*
import javax.inject.Inject

class GetAthleteUseCase @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    suspend fun getAthlete(
        context: Context,
        athleteId: Long,
        code: String
    ): Resource<AthleteEntity> {

        val athleteEntity = AthleteDatabase
            .getInstance(context.applicationContext)
            .athleteDao
            .getAthleteById(athleteId = athleteId)


        return when {
            // There is no previous entry in the ROOM database
            athleteEntity == null -> {
                getAthleteFromAuthorizationCode(code = code)
            }
            // There is a previous, expired entry
            isDateExpired(athleteEntity.receivedOn, 3) -> {
                // TODO add refresh
                getAthleteFromAuthorizationCode(code = code)
            }
                // There is a previous non-expired entry, return the oAuth2Entity
            else -> Resource.Success(athleteEntity)
        }

    }


    // Invoked privately to specifically
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

    private fun isDateExpired(
        lastDateUnixSeconds: Int,
        daysToExpiration: Int
    ) =
        ((GregorianCalendar().timeInMillis / 1000) - lastDateUnixSeconds) < (86400 * daysToExpiration)

}