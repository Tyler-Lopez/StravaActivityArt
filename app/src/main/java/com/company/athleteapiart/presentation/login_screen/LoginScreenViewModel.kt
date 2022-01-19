package com.company.athleteapiart.presentation.login_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.dao.OAuth2Dao
import com.company.athleteapiart.data.database.OAuth2Database
import com.company.athleteapiart.repository.ActivityRepository
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.Oauth2
import com.company.athleteapiart.util.Resource
import com.company.athleteapiart.util.clientSecret
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
    var isLoading = mutableStateOf(false)
    var oAuthDao = mutableStateOf<OAuth2Dao?>(null)

    fun getDao(context: Context) {
        val oAuthDao = OAuth2Database.getInstance(context.applicationContext).oAuth2Dao
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