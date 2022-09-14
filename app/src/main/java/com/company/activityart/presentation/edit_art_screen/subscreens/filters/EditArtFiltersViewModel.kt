package com.company.activityart.presentation.edit_art_screen.subscreens.filters

import com.company.activityart.architecture.BaseViewModel
import com.company.activityart.util.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditArtFiltersViewModel @Inject constructor(
    private val timeUtils: TimeUtils
) : BaseViewModel<EditArtFiltersViewState, EditArtFiltersViewEvent>() {

    override fun onEvent(event: EditArtFiltersViewEvent) {
        TODO("Not yet implemented")
    }

}