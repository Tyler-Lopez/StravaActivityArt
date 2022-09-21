package com.company.activityart.presentation.edit_art_screen.subscreens.preview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Size
import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseChildViewModel
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewEvent.*
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewState.*
import com.company.activityart.util.ActivityFilterUtils
import com.company.activityart.util.ImageSizeUtils
import com.company.activityart.util.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditArtPreviewViewModel @Inject constructor(
    private val getActivitiesFromCacheUseCase: GetActivitiesFromCacheUseCase,
    private val imageSizeUtils: ImageSizeUtils,
    private val activityFilterUtils: ActivityFilterUtils,
) : BaseChildViewModel<
        EditArtPreviewViewState,
        EditArtPreviewViewEvent,
        EditArtViewEvent
        >() {

    init {
        pushState(Loading)
    }

    override fun onEvent(event: EditArtPreviewViewEvent) {
        when (event) {
            is DrawArtRequested -> onDrawArtRequested(event)
        }
    }

    private fun onDrawArtRequested(event: DrawArtRequested) {
        viewModelScope.launch(Dispatchers.Default) {
            val sizeScaled = event.run {
                imageSizeUtils.sizeToMaximumSize(
                    actualSize = Size(targetWidthPx.toInt(), targetHeightPx.toInt()),
                    maximumSize = Size(screenWidthPx.toInt(), screenHeightPx.toInt())
                )
            }
            /*
            val filteredActivities = activityFilterUtils.filterActivities(
                activities = activities,
                unixSecondsRange = event.run {
                    unixSecondSelectedStart..unixSecondSelectedEnd
                },
                excludeActivityTypes = event.excludeActivityTypes
            )
            val activityCount = filteredActivities.size

             */

            pushState(
                Standby(
                    // Create a bitmap which will be drawn on by canvas and return
                    Bitmap.createBitmap(
                        sizeScaled.width,
                        sizeScaled.height,
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
                            canvas.drawText(
                                5.toString(),
                                0f,
                                150f,
                                Paint().also {
                                    it.color = Color.BLACK
                                    it.textSize = 50f
                                }
                            )
                        }
                    }
                )
            )
        }
    }
}