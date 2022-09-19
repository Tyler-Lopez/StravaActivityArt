package com.company.activityart.presentation.edit_art_screen.subscreens.preview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
        pushState(Standby(null))
    }

    override fun onEvent(event: EditArtPreviewViewEvent) {
        when (event) {
            is DrawArtRequested -> onDrawArtRequested(event)
        }
    }

    private fun onDrawArtRequested(event: DrawArtRequested) {
        viewModelScope.launch {
            
            pushState(
                Standby(
                    // Create a bitmap which will be drawn on by canvas and return
                    Bitmap.createBitmap(
                        event.screenWidthPx.toInt(),
                        event.screenHeightPx.toInt(),
                        Bitmap.Config.ARGB_8888
                    ).also { bitmap ->
                        Canvas(bitmap).also { canvas ->
                            canvas.drawRect(
                                0f,
                                0f,
                                canvas.width.toFloat(),
                                canvas.height.toFloat(),
                                Paint().also {
                                    it.color = Color.CYAN
                                }
                            )
                        }
                    }
                )
            )
        }
    }
}