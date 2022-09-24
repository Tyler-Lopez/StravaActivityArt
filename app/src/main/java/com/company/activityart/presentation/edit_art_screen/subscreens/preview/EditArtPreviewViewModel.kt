package com.company.activityart.presentation.edit_art_screen.subscreens.preview

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
import com.company.activityart.util.VisualizationUtils
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
    private val visualizationUtils: VisualizationUtils
) : BaseChildViewModel<
        EditArtPreviewViewState,
        EditArtPreviewViewEvent,
        EditArtViewEvent
        >() {

    private val activities: List<Activity> by lazy { getActivitiesFromCacheUseCase() }
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
            val sizeScaled = event.run {
                imageSizeUtils.sizeToMaximumSize(
                    actualSize = targetSize,
                    maximumSize = Size(screenWidth, screenHeight)
                )
            }

            val filteredActivities = activityFilterUtils.filterActivities(
                activities = activities,
                unixSecondsRange = event.run {
                    filterUnixSecondEnd..filterUnixSecondStart
                },
                excludeActivityTypes = event.filterExcludedTypes
            )

            /*
            pushState(Standby(visualizationUtils.createBitmap(
                activities = filteredActivities,
                bitmapSize = sizeScaled,
                colorActivities = event.styleActivities,
                colorBackground = event.styleBackground,
                paddingFraction = 0.05f,
                strokeWidthType = event.styleStrokeWidthType
            )))

             */
        }
    }
}