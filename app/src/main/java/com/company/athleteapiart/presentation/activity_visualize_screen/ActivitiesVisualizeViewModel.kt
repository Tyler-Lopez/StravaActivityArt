package com.company.athleteapiart.presentation.activity_visualize_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.company.athleteapiart.domain.repository.ActivityRepository
import com.company.athleteapiart.util.AthleteActivities
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivitiesVisualizeViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    var activities = AthleteActivities.filteredActivities
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    
}