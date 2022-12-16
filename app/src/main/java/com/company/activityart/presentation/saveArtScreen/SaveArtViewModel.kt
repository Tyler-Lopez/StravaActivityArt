package com.company.activityart.presentation.saveArtScreen

import androidx.lifecycle.SavedStateHandle
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.presentation.MainDestination
import com.company.activityart.util.NavArgSpecification.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaveArtViewModel @Inject constructor(
    gson: Gson,
    savedStateHandle: SavedStateHandle
) : BaseRoutingViewModel<SaveArtViewState, SaveArtViewEvent, MainDestination>() {

    init {
        val activityTypes = gson.fromJson<List<String>>(
            ActivityTypes.getArg(savedStateHandle),
            List::class.java
        )
        val colorActivities = ColorActivities.getArg(savedStateHandle)
        val colorBackground = ColorBackground.getArg(savedStateHandle)
        println("Color activities: $colorActivities")
    }


    override fun onEvent(event: SaveArtViewEvent) {
        TODO("Not yet implemented")
    }
}