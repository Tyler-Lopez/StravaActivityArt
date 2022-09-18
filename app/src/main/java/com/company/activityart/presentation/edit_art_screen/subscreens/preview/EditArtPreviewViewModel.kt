package com.company.activityart.presentation.edit_art_screen.subscreens.preview

import com.company.activityart.architecture.BaseChildViewModel
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewEvent
import com.company.activityart.presentation.edit_art_screen.subscreens.filters.EditArtFiltersViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditArtPreviewViewModel @Inject constructor(

) : BaseChildViewModel<
        EditArtPreviewViewState,
        EditArtPreviewViewEvent,
        EditArtViewEvent
        >()  {

    override fun onEvent(event: EditArtPreviewViewEvent) {
        TODO("Not yet implemented")
    }
}