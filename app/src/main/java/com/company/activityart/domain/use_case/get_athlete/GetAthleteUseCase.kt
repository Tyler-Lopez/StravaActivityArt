package com.company.activityart.domain.use_case.get_athlete

import android.content.Context
import com.company.activityart.data.database.AthleteDatabase
import com.company.activityart.data.entities.AthleteEntity
import com.company.activityart.data.remote.AthleteApi
import com.company.activityart.util.HTTPFault
import com.company.activityart.util.Resource
import java.util.*
import javax.inject.Inject

class GetAthleteUseCase @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    suspend fun getAthleteFromRoom(
        context: Context,
        athleteId: Long
    ) = AthleteDatabase
        .getInstance(context.applicationContext)
        .athleteDao
        .getAthleteById(athleteId = athleteId)


    suspend fun getAthlete(
        context: Context,
        athleteId: Long,
        code: String
    ): Resource<AthleteEntity> {

        val athleteEntity = AthleteDatabase
            .getInstance(context.applicationContext)
            .athleteDao
            .getAthleteById(athleteId = athleteId)

        println(athleteEntity.toString() + " athlete entity HERE")
        return when {
            // There is no previous entry in the ROOM database
            athleteEntity == null -> {
                getAthleteFromAuthorizationCode(code = code)
            }
            // There is a previous, expired entry
            isDateExpired(athleteEntity.receivedOn, 3) -> {
                // TODO add refresh
                println("Entity cached is expired")
                getAthleteFromAuthorizationCode(code = code, athleteEntity.yearMonthsCached)
            }
            // There is a previous non-expired entry, return the oAuth2Entity
            else -> {
                println("Here, there is a ROOM entry")
                Resource.Success(athleteEntity)
            }
        }

    }


    // Invoked privately to specifically
    private suspend fun getAthleteFromAuthorizationCode(
        code: String,
        previousCacheData: Map<Int, Int>? = null
    ): Resource<AthleteEntity> {
        val data = try {
            println("here success")
            api.getAuthenticatedAthlete(authHeader = "Bearer $code")
        } catch (e: Exception) {
            println("An error has occurred - ${e.message}")
            return Resource.Error(HTTPFault.getEnum(e.message))
        }

        return Resource.Success(
            AthleteEntity(
                athleteId = data.id,
                userName = data.username,
                receivedOn = (GregorianCalendar().timeInMillis / 1000).toInt(),
                firstName = data.firstname,
                profilePictureMedium = data.profile_medium,
                profilePictureLarge = data.profile,
                lastName = data.lastname,
                yearMonthsCached = previousCacheData ?: mapOf(),
                gears = mapOf()
            )
        )
    }

    // TODO come back to this11
    private fun isDateExpired(
        lastDateUnixSeconds: Int,
        daysToExpiration: Int
    ) = true
       // ((GregorianCalendar().timeInMillis / 1000) - lastDateUnixSeconds) > (86400 * daysToExpiration)

}