package com.company.activityart.presentation.edit_art_screen.subscreens.style

import com.company.activityart.architecture.BaseChildViewModel
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.edit_art_screen.ColorWrapper
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewEvent
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewState
import com.company.activityart.util.ActivityFilterUtils
import com.company.activityart.util.ImageSizeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditArtStyleViewModel @Inject constructor(
) : BaseChildViewModel<
        EditArtStyleViewState,
        EditArtStyleViewEvent,
        EditArtViewEvent
        >() {

    init {
        pushState(EditArtStyleViewState.Standby(
            ColorWrapper(0, 0, 0, 0)
        ))
    }

    override fun onEvent(event: EditArtStyleViewEvent) {
        TODO("Not yet implemented")
    }
}