package com.company.athleteapiart.presentation.time_select_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class TimeSelectViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    var activities = AthleteActivities.selectedActivities
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    var activitiesSize = mutableStateOf(AthleteActivities.activities.value.size)



    fun loadActivitiesByYear(year: Int) {
        val after = (GregorianCalendar(year, 0, 1).timeInMillis / 1000).toInt()
        val beforeDate = GregorianCalendar(year + 1, 0, 1)
        beforeDate.add(GregorianCalendar.DAY_OF_MONTH, -1)
        val before = (beforeDate.timeInMillis / 1000).toInt()

        if (isLoading.value) return
        isLoading.value = true
        println("here, before is $before after is $after")
        getActivities(
            page = 1,
            before = before,
            after = after
        )
    }

    private fun getActivities(page: Int, before: Int, after: Int) {
        viewModelScope.launch {
            loadError.value = ""
            if (Oauth2.accessToken == "null") {
                val result = repository.getAccessToken(
                    clientId = 75992,
                    clientSecret = clientSecret,
                    code = Oauth2.authorizationCode,
                    grantType = "authorization_code"
                )
                when (result) {
                    is Resource.Success -> {
                        Oauth2.accessToken = result.data.access_token
                    }
                    is Resource.Error -> {
                        if (result.message.contains("Unable to resolve host")) {
                            getActivities(page, before, after)
                        } else {
                            loadError.value = result.message
                            println("error occured ${loadError.value}")
                            isLoading.value = false
                        }
                     //   getActivities(page, before, after)
                    }
                }
            }
            when (val result = repository
                .getActivities(
                    page = page,
                    perPage = 100,
                    before = before,
                    after = after
                )
            ) {
                is Resource.Success -> {
                    if (result.data.isEmpty()) {
                        endReached.value = true
                        isLoading.value = false
                    }
                    else {
                        AthleteActivities.activities.value.addAll(result.data)
                        activitiesSize.value = AthleteActivities.activities.value.size
                        getActivities(page + 1, before, after)
                    }
                }
                is Resource.Error -> {
                    if (result.message.contains("timeout", true)) {
                        println("timed out trying again")
                        getActivities(page, before, after)
                    } else if (result.message.contains("Unable to resolve host")) {
                        println("unable to resolve host trying again")
                        getActivities(page, before, after)
                    } else {
                            //   getActivities(page, before, after)
                                println("error here")
                             loadError.value = result.message
                               isLoading.value = false
                        }
                    }
                }
            }
        }
}