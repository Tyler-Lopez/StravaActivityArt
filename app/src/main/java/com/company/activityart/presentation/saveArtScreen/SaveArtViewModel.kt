package com.company.activityart.presentation.saveArtScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import android.util.Size
import androidx.compose.material.SnackbarHostState
import androidx.core.graphics.scale
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.FileRepository
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.*
import com.company.activityart.presentation.editArtScreen.StrokeWidthType
import com.company.activityart.presentation.saveArtScreen.SaveArtViewState.*
import com.company.activityart.presentation.saveArtScreen.SaveArtViewEvent.*
import com.company.activityart.util.*
import com.company.activityart.util.NavArgSpecification.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveArtViewModel @Inject constructor(
    private val fileRepository: FileRepository,
    private val imageSizeUtils: ImageSizeUtils,
    private val visualizationUtils: VisualizationUtils,
    activitiesFromCacheUseCase: GetActivitiesFromCacheUseCase,
    gson: Gson,
    ssh: SavedStateHandle,
) : BaseRoutingViewModel<SaveArtViewState, SaveArtViewEvent, MainDestination>() {

    private val activityTypes = gson.fromJson<List<String>>(
        ActivityTypes.rawArg(ssh),
        List::class.java
    )
    private val activities = activitiesFromCacheUseCase()
    private val colorActivitiesArgb = ColorActivitiesArgb.rawArg(ssh).toInt()
    private val colorBackgroundArgb = ColorBackgroundArgb.rawArg(ssh).toInt()
    private val filterDateAfterMs = FilterDateAfterMs.rawArg(ssh).toLong()
    private val filterDateBeforeMs = FilterDateBeforeMs.rawArg(ssh).toLong()
    private val sizeHeightPx = SizeHeightPx.rawArg(ssh).toInt()
    private val sizeWidthPx = SizeWidthPx.rawArg(ssh).toInt()
    private val strokeWidthType = StrokeWidthType.valueOf(StrokeWidth.rawArg(ssh))

    override fun onEvent(event: SaveArtViewEvent) {
        when (event) {
            is ClickedDownload -> onClickedDownload()
            is ClickedNavigateUp -> onClickedNavigateUp()
            is ClickedShare -> onClickedShare()
            is ScreenMeasured -> onScreenMeasured(event)
        }
    }

    private fun onClickedDownload() {
        withStandbyState {
            copyDownloadStart().push()
            viewModelScope.launch(Dispatchers.IO) {
                fileRepository
                    .saveBitmapToGallery(bitmapDownloadSize)
                    .doOnSuccess {
                        copyDownloadTerminate().push()
                        snackbarHostState.showSnackbar("Downloaded successfully")
                    }
                    .doOnError {
                        println("Error was $exception")
                        copyDownloadTerminate().push()
                        snackbarHostState.showSnackbar("Download failed")
                    }
            }
        }
    }


    private fun onClickedNavigateUp() {
        viewModelScope.launch {
            routeTo(NavigateUp)
        }
    }

    private fun onClickedShare() {
        withStandbyState {
            viewModelScope.launch {
                fileRepository
                    .saveBitmapToCache(bitmapDownloadSize)
                    .doOnSuccess {
                        routeTo(ShareFile(data))
                    }
                    .doOnError {
                        println("Here, error $exception")
                        snackbarHostState.showSnackbar("Sharing failed")
                    }
            }
        }
    }

    private fun onScreenMeasured(event: ScreenMeasured) {
        Loading.push()
        viewModelScope.launch(Dispatchers.Default) {
            val bitmap = visualizationUtils.createBitmap(
                activities = activities.filter {
                    it.type in activityTypes
                },
                colorActivitiesArgb = colorActivitiesArgb,
                colorBackgroundArgb = colorBackgroundArgb,
                bitmapSize = Size(sizeWidthPx, sizeHeightPx),
                strokeWidthType = strokeWidthType
            )
            val newSize = imageSizeUtils.sizeToMaximumSize(
                Size(sizeWidthPx, sizeHeightPx),
                event.size,
            )
            val scaledBitmap = bitmap.scale(newSize.width, newSize.height)
            Standby(
                bitmapDownloadSize = bitmap,
                bitmapScreenSize = scaledBitmap,
                snackbarHostState = SnackbarHostState()
            ).push()
        }
    }

    private fun withStandbyState(onState: Standby.() -> Unit) {
        (lastPushedState as? Standby)?.apply(onState)
    }
}