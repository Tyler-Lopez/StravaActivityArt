package com.activityartapp.presentation.saveArtScreen

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import android.util.Size
import androidx.compose.material.SnackbarHostState
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.domain.repository.FileRepository
import com.activityartapp.domain.useCase.activities.GetActivitiesFromMemory
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.MainDestination.*
import com.activityartapp.presentation.editArtScreen.StrokeWidthType
import com.activityartapp.presentation.errorScreen.ErrorScreenType
import com.activityartapp.presentation.saveArtScreen.SaveArtViewState.*
import com.activityartapp.presentation.saveArtScreen.SaveArtViewEvent.*
import com.activityartapp.util.*
import com.activityartapp.util.NavArgSpecification.*
import com.activityartapp.util.enums.EditArtSortDirectionType
import com.activityartapp.util.enums.EditArtSortType
import com.activityartapp.util.enums.FontSizeType
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SaveArtViewModel @Inject constructor(
    private val activityFilterUtils: ActivityFilterUtils,
    private val fileRepository: FileRepository,
    private val imageSizeUtils: ImageSizeUtils,
    private val visualizationUtils: VisualizationUtils,
    getActivitiesFromMemory: GetActivitiesFromMemory,
    gson: Gson,
    ssh: SavedStateHandle,
) : BaseRoutingViewModel<SaveArtViewState, SaveArtViewEvent, MainDestination>() {
    
    private val activityTypes = gson.fromJson<List<String>>(
        ActivityTypes.rawArg(ssh),
        List::class.java
    )
    private val activities = getActivitiesFromMemory()
    private val colorActivitiesArgb = ColorActivitiesArgb.rawArg(ssh).toInt()
    private val colorBackgroundArgb = ColorBackgroundArgb.rawArg(ssh).toInt()
    private val colorFontArgb = ColorFontArgb.rawArg(ssh).toInt()
    private val filterDateAfterMs = FilterDateAfterMs.rawArg(ssh).toLong()
    private val filterDateBeforeMs = FilterDateBeforeMs.rawArg(ssh).toLong()
    private val filterDistanceLessThanMeters = FilterDistanceLessThanMeters.rawArg(ssh).toInt()
    private val filterDistanceMoreThanMeters = FilterDistanceMoreThanMeters.rawArg(ssh).toInt()
    private val sizeHeightPx = SizeHeightPx.rawArg(ssh).toInt()
    private val sizeWidthPx = SizeWidthPx.rawArg(ssh).toInt()
    private val sizePx: Size get() = Size(sizeWidthPx, sizeHeightPx)
    private val sortDirectionType = EditArtSortDirectionType.valueOf(SortDirectionType.rawArg(ssh))
    private val sortType = EditArtSortType.valueOf(SortType.rawArg(ssh))
    private val strokeWidthType = StrokeWidthType.valueOf(StrokeWidth.rawArg(ssh))
    private val textLeft = TextLeft.rawArg(ssh).takeIf { it.isNotBlank() }
    private val textCenter = TextCenter.rawArg(ssh).takeIf { it.isNotBlank() }
    private val textRight = TextRight.rawArg(ssh).takeIf { it.isNotBlank() }
    private val fontAssetPath = TextFontAssetPath.rawArg(ssh)
    private val fontTypeSize = FontSizeType.valueOf(TextFontSize.rawArg(ssh))

    override fun onEvent(event: SaveArtViewEvent) {
        when (event) {
            is ActivityResumed -> onActivityResumed()
            is ClickedDownload -> onClickedDownload()
            is ClickedDownloadWhenPermissionPermaDenied -> onClickedDownloadWhenPermissionPermaDenied()
            is ClickedNavigateUp -> onClickedNavigateUp()
            is ClickedShare -> onClickedShare()
            is ScreenMeasured -> onScreenMeasured(event)
        }
    }

    private fun onActivityResumed() {
        withStandbyState {
            copyShareTerminate().push()
        }
    }

    private fun onClickedDownload() {
        withStandbyState {
            copyDownloadStart().push()
            viewModelScope.launch(Dispatchers.IO) {
                viewModelScope.launch(Dispatchers.Default) {
                    fileRepository
                        .saveBitmapToGallery(createArtBitmapOfSize(sizePx))
                        .doOnSuccess {
                            snackbarHostState.showSnackbar("Downloaded successfully")
                        }
                        .doOnError {
                            println("ApiError was $exception")
                            snackbarHostState.showSnackbar("Download failed")
                        }
                        .also { copyDownloadTerminate().push() }
                }
            }
        }
    }

    private fun onClickedDownloadWhenPermissionPermaDenied() {
        viewModelScope.launch {
            routeTo(
                NavigateError(
                    clearNavigationHistory = false,
                    errorScreenType = ErrorScreenType.PERMISSION_DENIED
                )
            )
        }
    }

    private fun onClickedNavigateUp() {
        viewModelScope.launch {
            routeTo(NavigateUp)
        }
    }

    private fun onClickedShare() {
        withStandbyState {
            copyShareStart().push()
            viewModelScope.launch(Dispatchers.IO) {
                fileRepository
                    .saveBitmapToCache(createArtBitmapOfSize(sizePx))
                    .doOnSuccess {
                        withContext(Dispatchers.Main) {
                            routeTo(ShareFile(data))
                        }
                    }
                    .doOnError {
                        copyDownloadTerminate().push()
                        snackbarHostState.showSnackbar("Sharing failed")
                    }
            }
        }
    }

    private fun onScreenMeasured(event: ScreenMeasured) {
        Loading.push()
        viewModelScope.launch(Dispatchers.Default) {
            Standby(
                bitmapScreenSize = createArtBitmapOfSize(
                    imageSizeUtils.sizeToMaximumSize(
                        actualSize = sizePx,
                        maximumSize = event.size
                    )
                ),
                snackbarHostState = SnackbarHostState()
            ).push()
        }
    }

    private fun withStandbyState(onState: Standby.() -> Unit) {
        (lastPushedState as? Standby)?.apply(onState)
    }

    private fun createArtBitmapOfSize(size: Size): Bitmap {
        return visualizationUtils.createBitmap(
            activities = activityFilterUtils.filterActivities(
                activities = activities,
                includeActivityTypes = activityTypes.toSet(),
                unixMsRange = filterDateAfterMs..filterDateBeforeMs,
                distanceRange = filterDistanceMoreThanMeters..filterDistanceLessThanMeters
            ),
            fontAssetPath = fontAssetPath,
            fontSize = fontTypeSize,
            colorActivitiesArgb = colorActivitiesArgb,
            colorBackgroundArgb = colorBackgroundArgb,
            colorFontArgb = colorFontArgb,
            bitmapSize = size,
            sortType = sortType,
            sortDirectionType = sortDirectionType,
            strokeWidth = strokeWidthType,
            textLeft = textLeft,
            textCenter = textCenter,
            textRight = textRight,
        )
    }
}