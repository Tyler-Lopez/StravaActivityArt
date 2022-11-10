package com.company.activityart.presentation.saveArtScreen

import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.presentation.MainDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaveArtViewModel @Inject constructor(
) : BaseRoutingViewModel<SaveArtViewState, SaveArtViewEvent, MainDestination>() {
    override fun onEvent(event: SaveArtViewEvent) {
        TODO("Not yet implemented")
    }
}