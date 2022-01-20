package com.company.athleteapiart.presentation.activity_select_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.repository.ActivityRepository
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.OAuth2
import com.company.athleteapiart.util.Resource
import com.company.athleteapiart.util.clientSecret
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivitySelectViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {


    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var page = 1

    init {
        if (AthleteActivities.activities.value.isEmpty())
            getActivities(page)
    }

    fun loadMoreActivities() {
        getActivities(page)
    }

    private fun getActivities(page: Int) {
        viewModelScope.launch {
            isLoading.value = true
            if (OAuth2.accessToken == "null") {
                val result = repository.getAccessToken(
                    clientId = 75992,
                    clientSecret = clientSecret,
                    code = OAuth2.authorizationCode,
                    grantType = "authorization_code"
                )
                when (result) {
                    is Resource.Success -> {
                        OAuth2.accessToken = result.data.access_token
                    }
                    is Resource.Error -> {
                        loadError.value = result.message
                        isLoading.value = false
                        return@launch
                    }
                }
            }
            when (val result = repository.getActivities(page = page, perPage = 0, 0, 0)) {
                is Resource.Success -> {
                    AthleteActivities.activities.value.addAll(result.data)
                    this@ActivitySelectViewModel.page++
                }
                is Resource.Error -> {
                    loadError.value = result.message
                    isLoading.value = false
                }
            }


            isLoading.value = false
            /*
            when (val result = repository.getActivities()) {
                is Resource.Success -> {
                    AthleteActivities.activities.value = result.data
                    isLoading.value = false
                }
                is Resource.Error -> {
                    loadError.value = result.message
                    isLoading.value = false
                }
            }

             */
        }
    }
}