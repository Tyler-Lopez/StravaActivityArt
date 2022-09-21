package com.company.activityart.presentation.about_screen

import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.about_screen.AboutViewState.*
import com.company.activityart.presentation.about_screen.AboutViewEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        viewModelScope.launch(Dispatchers.Default) {
            when (event) {
                is NavigateUpClicked -> onNavigateUpClicked()
            }
        }
    }

    private suspend fun onNavigateUpClicked() {
        routeTo(NavigateUp)
    }
}