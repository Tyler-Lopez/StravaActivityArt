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
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewEvent.DrawArtRequested
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewState.Loading
import com.company.activityart.presentation.edit_art_screen.subscreens.preview.EditArtPreviewViewState.Standby
import com.company.activityart.util.ActivityFilterUtils
import com.company.activityart.util.ImageSizeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    private val activities: List<Activity> by lazy { getActivitiesFromCacheUseCase().flatMap { it.value } }
    private var activityDrawJob: Job? = null

    init {
        pushState(Loading)
    }

    override fun onEvent(event: EditArtPreviewViewEvent) {
        when (event) {
            is DrawArtRequested -> onDrawArtRequested(event)
        }
    }

    private fun onDrawArtRequested(event: DrawArtRequested) {
        pushState(Loading)
        activityDrawJob?.cancel()
        activityDrawJob = viewModelScope.launch(Dispatchers.Default) {
            println("scaling ${event.targetSize} to ${event.screenWidth} and ${event.screenHeight}")
            val sizeScaled = event.run {
                imageSizeUtils.sizeToMaximumSize(
                    actualSize = targetSize,
                    maximumSize = Size(screenWidth, screenHeight)
                )
            }
            println("actually scaled to $sizeScaled")

            val filteredActivities = activityFilterUtils.filterActivities(
                activities = activities,
                unixSecondsRange = event.run {
                    filterUnixSecondEnd..filterUnixSecondStart
                },
                excludeActivityTypes = event.filterExcludedTypes
            )
            val activityCount = filteredActivities.size

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
                                    it.color = event.styleBackground.run {
                                        Color.argb(alpha, red, green, blue)
                                    }
                                }
                            )
                            canvas.drawText(
                                activityCount.toString(),
                                0f,
                                150f,
                                Paint().also {
                                    it.color = event.styleActivities.run {
                                        Color.argb(alpha, red, green, blue)
                                    }
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