package com.company.activityart.presentation.saveArtScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.editArtScreen.StrokeWidthType
import com.company.activityart.presentation.saveArtScreen.SaveArtViewState.*
import com.company.activityart.presentation.saveArtScreen.SaveArtViewEvent.ClickedNavigateUp
import com.company.activityart.util.NavArgSpecification
import com.company.activityart.util.NavArgSpecification.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveArtViewModel @Inject constructor(
    gson: Gson,
    ssh: SavedStateHandle
) : BaseRoutingViewModel<SaveArtViewState, SaveArtViewEvent, MainDestination>() {

    init {
        Loading.push()
        val activityTypes = gson.fromJson<List<String>>(
            ActivityTypes.rawArg(ssh),
            List::class.java
        )
        val colorActivitiesArgb = ColorActivitiesArgb.rawArg(ssh).toInt()
        val colorBackgroundArgb = ColorBackgroundArgb.rawArg(ssh).toInt()
        val filterDateAfterMs = FilterDateAfterMs.rawArg(ssh).toLong()
        val filterDateBeforeMs = FilterDateBeforeMs.rawArg(ssh).toLong()
        val sizeHeightPx = SizeHeightPx.rawArg(ssh).toInt()
        val sizeWidthPx = SizeWidthPx.rawArg(ssh).toInt()
        val strokeWidthType = StrokeWidthType.valueOf(StrokeWidth.rawArg(ssh))
    }


    override fun onEvent(event: SaveArtViewEvent) {
        when (event) {
            is ClickedNavigateUp -> onClickedNavigateUp()
        }
    }

    private fun onClickedNavigateUp() {
        viewModelScope.launch {
            routeTo(NavigateUp)
        }
    }
}