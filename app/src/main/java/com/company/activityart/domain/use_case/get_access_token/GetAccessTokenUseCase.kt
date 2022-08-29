package com.company.activityart.domain.use_case.get_access_token

/*
class GetAccessTokenWithRefreshUseCase @Inject constructor(
    private val api: AthleteApi // Impl of API
) {
    private val clientId = 75992

    // Invoked publicly, checks Room database for previous entry
    // Returns error if not yet connected
    suspend fun getAccessToken(context: Context): Resource<OAuth2Entity> {

        val oAuth2Entity = OAuth2Database
            .getInstance(context.applicationContext)
            .oAuth2Dao
            .getOauth2()

        println("OAUTH2ENTITY IS ${oAuth2Entity}")
        return when {
            // There is no previous entry in the ROOM database
            oAuth2Entity == null -> Resource.Failure(HTTPFault.UNAUTHORIZED)
            // There is a previous, expired entry
            accessTokenIsExpired(oAuth2Entity.receivedOn) -> {
                // Attempt to refresh the access token
                val response =
                    getAccessTokenFromRefreshToken(
                        clientId = clientId,
                        clientSecret = CLIENT_SECRET,
                        code = oAuth2Entity.refreshToken
                    )
                when (response) {
                    // Successfully refreshed the token
                    is Resource.Success -> {
                        val data = response.data
                        println(data)
                        val receivedOAuth = OAuth2Entity(
                            athleteId = oAuth2Entity.athleteId,
                            receivedOn = data.expires_at,
                            accessToken = data.access_token,
                            refreshToken = data.refresh_token
                        )
                        Resource.Success(
                            data = receivedOAuth
                        )
                    }
                    // Was not able to successfully refresh the token
                    else -> Resource.Failure(HTTPFault.UNKNOWN)
                }
            }
            // There is a previous non-expired entry, return the oAuth2Entity
            else -> Resource.Success(oAuth2Entity)
        }
    }

    suspend fun getAccessTokenFromAuthorizationCode(
        code: String
    ): Resource<OAuth2Entity> {
        val data = try {
            api.getAccessToken(
                clientId = clientId,
                clientSecret = CLIENT_SECRET,
                code = code,
                grantType = "authorization_code"
            )
        } catch (e: Exception) {
            println("An error has occurred - ${e.message}")
            return Resource.Failure(HTTPFault.getEnum(e.message))
        }
        return Resource.Success(
            OAuth2Entity(
                athleteId = data.athlete.id,
                receivedOn = data.expires_at,
                accessToken = data.access_token,
                refreshToken = data.refresh_token
            )
        )
    }

    // Invoked privately only if the Room database access token is expired
    private suspend fun getAccessTokenFromRefreshToken(
        clientId: Int,
        clientSecret: String,
        code: String,
    ): Resource<Bearer> {

        val response = try {
            api.getAccessTokenFromRefresh(
                clientId = clientId,
                clientSecret = clientSecret,
                refreshToken = code
            )
        } catch (e: Exception) {
            println("ERROR OCCURRED GETTING REFRESH TOKEN")
            return Resource.Failure(HTTPFault.getEnum(e.message))
        }

        return Resource.Success(response)
    }

    // Returns TRUE if an access token is 20k seconds old
    private fun accessTokenIsExpired(time: Int): Boolean {
        val now = (GregorianCalendar().timeInMillis / 1000).toInt()
        return (now - time >= 20000)
    }
}

 */