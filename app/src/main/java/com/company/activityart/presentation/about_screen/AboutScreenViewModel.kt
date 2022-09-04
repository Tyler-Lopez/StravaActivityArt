package com.company.activityart.presentation.about_screen

import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.about_screen.AboutScreenViewState.*
import com.company.activityart.presentation.about_screen.AboutScreenViewEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class AboutScreenViewModel : BaseRoutingViewModel<
        AboutScreenViewState,
        AboutScreenViewEvent,
        MainDestination
        >() {

    init {
        pushState(Standby)
    }

    override fun onEvent(event: AboutScreenViewEvent) {
        when (event) {
            is NavigateUpClicked -> onNavigateUpClicked()
        }
    }

    private fun onNavigateUpClicked() {
        routeTo(NavigateUp)
    }

    override fun onRouterAttached() {} // No-op
}