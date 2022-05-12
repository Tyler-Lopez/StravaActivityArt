package com.company.athleteapiart.presentation.filter_gear_screen

import androidx.lifecycle.ViewModel
import com.company.athleteapiart.domain.use_case.ActivitiesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterGearViewModel @Inject constructor(
    activitiesUseCases: ActivitiesUseCases
) : ViewModel() {

}