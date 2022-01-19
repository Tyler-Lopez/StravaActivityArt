package com.company.athleteapiart.presentation.login_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.dao.OAuth2Dao
import com.company.athleteapiart.data.database.OAuth2Database
import com.company.athleteapiart.data.entities.OAuth2Entity
import com.company.athleteapiart.repository.ActivityRepository
import com.company.athleteapiart.util.*
import com.google.android.gms.common.util.CollectionUtils.setOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.reflect.Array.set
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    var isLoading = mutableStateOf(true)
    var requestLogin = mutableStateOf(false)
    private val oAuthDao = mutableStateOf<OAuth2Dao?>(null)


    fun loadDao(context: Context) {
        viewModelScope.launch {
            oAuthDao.value = OAuth2Database.getInstance(context.applicationContext).oAuth2Dao
            getAccessToken(oAuthDao.value?.getOauth2())
        }
    }

    private fun getAccessToken(oAuth2Entity: OAuth2Entity?) {
        viewModelScope.launch {
            when {
                // If the user just authorized with Strava...
                Oauth2.authorizationCode != "null" -> {
                    val result = repository.getAccessToken(
                        clientId = 75992,
                        clientSecret = clientSecret,
                        code = Oauth2.authorizationCode,
                        grantType = "authorization_code"
                    )
                    when (result) {
                        is Resource.Success -> {
                            Oauth2.accessToken = result.data.access_token
                            oAuthDao.value!!.clearOauth2()
                            oAuthDao.value!!.insertOauth2(
                                OAuth2Entity(
                                    receivedOn = (GregorianCalendar().timeInMillis / 1000).toInt(),
                                    accessToken = result.data.access_token,
                                    refreshToken = result.data.refresh_token
                                )
                            )
                            requestLogin.value = false
                            isLoading.value = false
                        }
                        is Resource.Error -> {
                            //   isLoading.value = false
                            //   return@launch
                        }
                    }
                }
                oAuth2Entity == null -> {
                    // Determine that we do not have access token before, ask user to connect
                    requestLogin.value = true
                    isLoading.value = false
                }
                TimeUtils.accessTokenExpired(oAuth2Entity.receivedOn) -> {
                    // Get refresh token

                }
                else -> {
                    // Not expired, use token
                    Oauth2.accessToken = oAuth2Entity.accessToken
                    requestLogin.value = false
                    isLoading.value = false
                }
            }
        }
    }

    private val intentUri: Uri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
        .buildUpon()
        .appendQueryParameter("client_id", "75992")
        .appendQueryParameter("redirect_uri", "com.company.athleteapiart://myapp.com")
        .appendQueryParameter("response_type", "code")
        .appendQueryParameter("approval_prompt", "auto")
        .appendQueryParameter("scope", "read,read_all,activity:read,activity:read_all")
        .build()

    val loginIntent = Intent(Intent.ACTION_VIEW, intentUri)
}