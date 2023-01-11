package com.activityartapp.presentation.aboutScreen

import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.MainDestination.NavigateUp
import com.activityartapp.presentation.aboutScreen.AboutViewEvent.NavigateUpClicked
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
        pushState(AboutViewState)
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