package com.company.athleteapiart.presentation.athletescreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.repository.ActivityRepository
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.Oauth2
import com.company.athleteapiart.util.Resource
import com.company.athleteapiart.util.clientSecret
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AthleteViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init {

    }

    fun getAccessToken() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getAccessToken(
                clientId = 75992,
                clientSecret = clientSecret,
                code = Oauth2.authorizationCode,
                grantType = "authorization_code"
            )
            when (result) {
                is Resource.Success -> {
                    Oauth2.accessToken = result.data.access_token
                    isLoading.value = false
                    getActivities()
                }
                is Resource.Error -> {
                    loadError.value = result.message
                    isLoading.value = false
                }
            }
        }
    }

    private fun getActivities() {
        viewModelScope.launch {
            when (val result = repository.getActivities()) {
                is Resource.Success -> {
                    AthleteActivities.activities.value = result.data
                    isLoading.value = false
                }
                is Resource.Error -> {
                    isLoading.value = false
                }
            }
        }
    }
}