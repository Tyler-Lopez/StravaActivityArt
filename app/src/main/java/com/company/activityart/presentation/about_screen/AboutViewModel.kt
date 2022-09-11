package com.company.activityart.presentation.about_screen

import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.about_screen.AboutViewState.*
import com.company.activityart.presentation.about_screen.AboutViewEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
) : BaseRoutingViewModel<
        AboutViewState,
        AboutViewEvent,
        MainDestination
        >() {

    init {
        pushState(Standby)
    }

    override fun onEvent(event: AboutViewEvent) {
        when (event) {
            is NavigateUpClicked -> onNavigateUpClicked()
        }
    }

    private fun onNavigateUpClicked() {
        routeTo(NavigateUp)
    }
}