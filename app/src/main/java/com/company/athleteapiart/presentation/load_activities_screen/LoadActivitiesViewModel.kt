package com.company.athleteapiart.presentation.load_activities_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.remote.responses.Activity
import com.company.athleteapiart.repository.ActivityRepository
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.OAuth2
import com.company.athleteapiart.util.Resource
import com.company.athleteapiart.util.clientSecret
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoadActivitiesViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    var activities = mutableStateOf<MutableList<Activity>>(mutableListOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    var activitiesSize = mutableStateOf(activities.value.size)


    fun loadActivitiesByYear(year: Int) {
        // Ensure we don't constantly invoke this
        // May be a better way to do this?
        // Overly cautious to not recursively invoke api right now
        if (isLoading.value || endReached.value) return
        isLoading.value = true


        viewModelScope.launch {
            val after = (GregorianCalendar(year, 0, 1).timeInMillis / 1000).toInt()
            val beforeDate = GregorianCalendar(year + 1, 0, 1)
            beforeDate.add(GregorianCalendar.DAY_OF_MONTH, -1)
            val before = (beforeDate.timeInMillis / 1000).toInt()

            println("here, before is $before after is $after")
            getActivities(
                page = 1,
                before = before,
                after = after
            )
        }
    }

    private fun getActivities(page: Int, before: Int, after: Int) {
        viewModelScope.launch {
            when (val result = repository
                .getActivities(
                    page = page,
                    perPage = 100,
                    before = before,
                    after = after
                )
            ) {
                is Resource.Success -> {
                    if (result.data.size < 100) {
                        activities.value.addAll(result.data)
                        endReached.value = true
                        isLoading.value = false
                    }
                    else {
                        activities.value.addAll(result.data)
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
                        endReached.value = true
                    }
                }
            }
        }
    }
}