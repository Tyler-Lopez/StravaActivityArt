package com.company.activityart.presentation

import androidx.lifecycle.ViewModel
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.architecture.Router
import com.company.activityart.architecture.StateSender
import com.company.activityart.domain.use_case.athlete.GetAthleteUseCase
import com.company.activityart.domain.use_case.authentication.ClearAccessTokenUseCase
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewEvent
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel(), EventReceiver<MainViewEvent>, Router<MainDestination> {


    override fun onEvent(event: MainViewEvent) {
        TODO("Not yet implemented")
    }

    override fun navigateTo(destination: MainDestination) {
        TODO("Not yet implemented")
    }

}