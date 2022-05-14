package com.company.athleteapiart.presentation.filter_gear_screen

import androidx.lifecycle.ViewModel
import com.company.athleteapiart.domain.use_case.ActivitiesUseCases
import com.company.athleteapiart.domain.use_case.AthleteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterGearViewModel @Inject constructor(
    activitiesUseCases: ActivitiesUseCases,
    athleteUseCases: AthleteUseCases
) : ViewModel() {

    // Use cases
    private val getActivitiesUseCase = activitiesUseCases.getActivitiesUseCase
    private val getAthleteUseCase = athleteUseCases.getAthleteUseCase

}