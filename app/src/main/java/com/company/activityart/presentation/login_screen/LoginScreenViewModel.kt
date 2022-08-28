package com.company.activityart.presentation.login_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.activityart.data.entities.OAuth2Entity
import com.company.activityart.domain.use_case.AuthenticationUseCases
import com.company.activityart.util.*
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

    // Use cases
    private val getAccessTokenUseCase = authenticationUseCases.getAccessTokenUseCase
    private val setAccessTokenUseCase = authenticationUseCases.setAccessTokenUseCase

    // State - observed in the view
    val loginScreenState = mutableStateOf(LoginScreenState.LAUNCH)

    private var oAuth2Entity: OAuth2Entity? = null
    val loginIntent = Intent(Intent.ACTION_VIEW, intentUri)

    fun getNavArgs(): Array<String> =
        arrayOf(oAuth2Entity?.athleteId.toString(), oAuth2Entity?.accessToken ?: "null")


    init {
        println("Init invoked on loginScreenViewModel state is ${loginScreenState.value}")
    }
    /*

    FUNCTION: attemptGetAccessToken
    Invoked when screen is in a LAUNCH state
    Attempts to obtain a valid access token from both ROOM and URI intent

     */

    fun attemptGetAccessToken(
        uri: Uri?,
        context: Context
    ) {
        viewModelScope.launch {

            // Set state of screen to loading
            loginScreenState.value = LoginScreenState.LOADING

            // Attempt to receive access token from ROOM database
            val responseRoom = getAccessTokenUseCase.getAccessToken(context)

            // Update access token with whatever result was
            oAuth2Entity = responseRoom.data

            println("Here, response room is ${responseRoom.data}")
            when (responseRoom) {

                // Successfully received
                is Resource.Success -> {
                    // Update ROOM database
                    setAccessTokenUseCase.setAccessToken(context, responseRoom.data)
                    loginScreenState.value = LoginScreenState.AUTHORIZED
                }
                // Not able to receive access token from ROOM database
                is Resource.Failure -> {
                    when {
                        // We just connected with Strava but have not parsed or done work with code
                        uri != null -> {
                            println("Here, uri is not null it is $uri")
                            // URI --> Authentication Code --> Access Token
                            // If successful, also add to ROOM in Use Case
                            val responseCode = authenticationUseCases
                                .getAccessTokenUseCase
                                .getAccessTokenFromAuthorizationCode(parseUri(uri))

                            // Update access token with whatever result was
                            oAuth2Entity = responseCode.data

                            when (responseCode) {
                                is Resource.Success -> {
                                    setAccessTokenUseCase.setAccessToken(context, responseCode.data)
                                    loginScreenState.value = LoginScreenState.AUTHORIZED
                                }
                                is Resource.Failure -> {
                                    println("Response code from uri is error")
                                }
                            }
                        }
                        // We have not yet connected with Strava
                        else -> loginScreenState.value = LoginScreenState.STANDBY
                    }
                }
            }
        }
    }

    // Invoked to parse authorization code from the URI string from intent
    private fun parseUri(uri: Uri): String =
        uri.toString().substring(43).substringBefore('&')

}