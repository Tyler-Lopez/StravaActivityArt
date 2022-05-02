package com.company.athleteapiart.presentation.filter_activities_screen

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.util.AthleteActivities
import com.company.athleteapiart.util.monthFromIso8601
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterActivitiesViewModel @Inject constructor(
  //  private val repository: ActivityRepository
) : ViewModel() {

    var isLoading = mutableStateOf(false)
    var checkboxes = mutableStateMapOf<String, Boolean>()
    var minDistanceSlider = mutableStateOf(0f)
    var maxDistanceSlider = mutableStateOf(0f)


    var months = mutableSetOf<String>()
    var activityTypes = mutableSetOf<String>()
    var minimumDistance = 0f
    var maximumDistance = 0f

    init {
        isLoading.value = true
        processActivities()
    }

    fun flipValue(key: String) {
        checkboxes[key] = !checkboxes.getOrDefault(key, false)
    }

    private fun processActivities() {
        viewModelScope.launch {
            val activities = AthleteActivities.activities
            for (activity in activities.value) {
                months.add(activity.start_date_local.monthFromIso8601())
                activityTypes.add(activity.type)
                if (minimumDistance > activity.distance) minimumDistance = activity
                    .distance
                    .toFloat()
                if (maximumDistance < activity.distance) maximumDistance = activity
                    .distance
                    .toFloat()
            }
            for (month in months) {
                checkboxes[month] = true
            }
            for (activity in activityTypes) {
                checkboxes[activity] = true
            }
            setMaxDistanceSlider(maximumDistance)
            isLoading.value = false
        }
    }

    fun setMaxDistanceSlider(distance: Float) {
        maxDistanceSlider.value = distance
    }

    fun setMinDistanceSlider(distance: Float) {
        minDistanceSlider.value = distance
    }

}