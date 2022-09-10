package com.company.activityart.presentation.make_art_screen

import androidx.lifecycle.SavedStateHandle
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.make_art_screen.MakeArtViewState.*
import com.company.activityart.presentation.make_art_screen.MakeArtViewEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MakeArtViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): BaseRoutingViewModel<MakeArtViewState, MakeArtViewEvent, MainDestination>() {

    override fun onRouterAttached() {
        TODO("Not yet implemented")
    }

    override fun onEvent(event: MakeArtViewEvent) {
        when (event) {
            is MakeFullscreenClicked -> {}
            is NavigateUpClicked -> {}
            is SaveClicked -> {}
            is SelectFiltersClicked -> {}
            is SelectStylesClicked -> {}
        }
    }
}