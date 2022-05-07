package com.company.athleteapiart.presentation.welcome_screen

import androidx.lifecycle.ViewModel
import com.company.athleteapiart.domain.use_case.AthleteUseCases
import com.company.athleteapiart.domain.use_case.AuthenticationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val athleteUseCases: AthleteUseCases
) : ViewModel() {

}
