package com.company.activityart.presentation.edit_art_screen.subscreens.preview

import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseChildViewModel
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewEvent.*
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditArtPreviewViewModel @Inject constructor(
    private val activitiesFromCacheUseCase: GetActivitiesFromCacheUseCase
) : BaseChildViewModel<
        EditArtPreviewViewState,
        EditArtPreviewViewEvent,
        EditArtViewEvent
        >()  {


    init {
        pushState(Loading)
    }

    override fun onEvent(event: EditArtPreviewViewEvent) {
        when (event) {
            is DrawArtRequested -> onDrawArtRequested(event)
        }
    }

    private fun onDrawArtRequested(event: DrawArtRequested) {

    }
}