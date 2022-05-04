package com.company.athleteapiart.presentation.login_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.database.OAuth2Database
import com.company.athleteapiart.domain.model.OAuth2
import com.company.athleteapiart.domain.use_case.AuthenticationUseCases
import com.company.athleteapiart.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases,
) : ViewModel() {

    private val intentUri: Uri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
        .buildUpon()
        .appendQueryParameter("client_id", "75992")
        .appendQueryParameter("redirect_uri", "com.company.athleteapiart://myapp.com")
        .appendQueryParameter("response_type", "code")
        .appendQueryParameter("approval_prompt", "auto")
        .appendQueryParameter("scope", "activity:read,activity:read_all")
        .build()

    val loginScreenState = mutableStateOf(LoginScreenState.LAUNCH)
    val authorizationCode: MutableState<String?> = mutableStateOf(null)
    val loginIntent = Intent(Intent.ACTION_VIEW, intentUri)


    /*
    var isLoading = mutableStateOf(false)
    var requestLogin = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private val oAuthDao = mutableStateOf<OAuth2Dao?>(null)


    fun loadDao(context: Context) {
        isLoading.value = true
        viewModelScope.launch {
            oAuthDao.value = OAuth2Database.getInstance(context.applicationContext).oAuth2Dao
            getAccessTokenFromAuthorizationCode(oAuthDao.value?.getOauth2())
        }
    }

    private fun getAccessTokenFromAuthorizationCode(oAuth2Entity: OAuth2Entity?) {
        viewModelScope.launch {
            println(OAuth2Legacy.authorizationCode)
            when {
                // If the user just authorized with Strava...
                OAuth2Legacy.authorizationCode != "null" -> {
                    val result = repository.getAccessTokenFromAuthorizationCode(
                        clientId = 75992,
                        clientSecret = clientSecret,
                        code = OAuth2Legacy.authorizationCode,
                        grantType = "authorization_code"
                    )
                    when (result) {
                        is Resource.Success -> {
                            oAuthDao.value!!.clearOauth2()
                            oAuthDao.value!!.insertOauth2(
                                OAuth2Entity(
                                    receivedOn = (GregorianCalendar().timeInMillis / 1000).toInt(),
                                    firstName = result.data.athlete.firstname,
                                    lastName = result.data.athlete.lastname,
                                    accessToken = result.data.access_token,
                                    refreshToken = result.data.refresh_token
                                )
                            )
                            OAuth2Legacy.accessToken = result.data.access_token
                            AthleteActivities.formatting.value.leftString =
                                "${result.data.athlete.firstname} ${result.data.athlete.lastname}"
                            requestLogin.value = false
                            endReached.value = true
                            isLoading.value = false
                        }
                        is Resource.Error -> {
                            //   isLoading.value = false
                            //   return@launch
                        }
                    }
                }
                oAuth2Entity == null || OAuth2Legacy.authorizationCode == "null" -> {
                    // Determine that we do not have access token before, ask user to connect
                    println("No access token, user should connect")
                    requestLogin.value = true
                    endReached.value = true
                    isLoading.value = false
                }
                TimeUtils.accessTokenExpired(oAuth2Entity.receivedOn) -> {
                    println("HERE HERE")
                    println("ACCESS TOKEN IN NEED OF REFRESH")
                    // Get refresh token
                    val result = repository.getAccessTokenFromRefresh(
                        clientId = 75992,
                        clientSecret = clientSecret,
                        refreshToken = oAuth2Entity.refreshToken,
                        grantType = "refresh_token"
                    )
                    when (result) {
                        is Resource.Success -> {
                            OAuth2Legacy.accessToken = result.data.access_token
                            oAuthDao.value!!.clearOauth2()
                            oAuthDao.value!!.insertOauth2(
                                OAuth2Entity(
                                    receivedOn = (GregorianCalendar().timeInMillis / 1000).toInt(),
                                    firstName = result.data.athlete.firstname,
                                    lastName = result.data.athlete.lastname,
                                    accessToken = result.data.access_token,
                                    refreshToken = result.data.refresh_token
                                )
                            )
                            AthleteActivities.formatting.value.leftString =
                                "${oAuth2Entity.firstName} ${oAuth2Entity.lastName}"
                            requestLogin.value = false
                            isLoading.value = false
                        }
                        is Resource.Error -> {
                            println(result.message + " ERROR ON FETCH REFRESH")
                            //   isLoading.value = false
                            //   return@launch
                        }
                    }

                }
                else -> {
                    // Not expired, use token
                    AthleteActivities.formatting.value.leftString =
                        "${oAuth2Entity.firstName} ${oAuth2Entity.lastName}"
                    OAuth2Legacy.accessToken = oAuth2Entity.accessToken
                    requestLogin.value = false
                    isLoading.value = false
                }
            }
        }
    }

     */

    fun attemptGetAccessToken(
        uri: Uri?,
        context: Context
    ) {
        viewModelScope.launch {

            val oAuth2Entity =
                authenticationUseCases.getAccessTokenUseCase.getAccessToken(context)

            // Ensure URI is not null
            when {
                authorizationCode.value != null -> {
                    loginScreenState.value = LoginScreenState.AUTHORIZED
                }
                // We just connected with Strava
                uri != null -> {
                    // Set state of screen to loading
                    loginScreenState.value = LoginScreenState.LOADING

                    // Parse URI into the access code as a string
                    authorizationCode.value = parseUri(uri)
                    loginScreenState.value = LoginScreenState.AUTHORIZED
                }
                // We received an access token
                oAuth2Entity != null -> {

                }
                // We have not yet connected with Strava
                else -> loginScreenState.value = LoginScreenState.STANDBY

            }
        }
    }

    private fun parseUri(uri: Uri): String =
        uri.toString().substring(43).substringBefore('&')

}