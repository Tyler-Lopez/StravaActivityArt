package com.company.athleteapiart.presentation.filter_activities_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.repository.ActivityRepository
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.monthFromIso8601
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterActivitiesViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    var isLoading = mutableStateOf(false)

    var months = mutableSetOf<String>()
    var activityTypes = mutableSetOf<String>()
    var activitySize = 0

    init {
        isLoading.value = true
        processActivities()
    }

    private fun processActivities() {
        viewModelScope.launch {
            val activities = AthleteActivities.activities
            activitySize = activities.value.size
            for (activity in activities.value) {
                months.add(activity.start_date_local.monthFromIso8601())
                activityTypes.add(activity.type)
            }
            isLoading.value = false
        }
    }

}