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
import com.company.activityart.presentation.editArtScreen.EditArtViewModel
import com.company.activityart.presentation.editArtScreen.StrokeWidthType
import com.company.activityart.presentation.saveArtScreen.SaveArtViewState.*
import com.company.activityart.presentation.saveArtScreen.SaveArtViewEvent.*
import com.company.activityart.util.*
import com.company.activityart.util.NavArgSpecification.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.roundToLong

@HiltViewModel
class SaveArtViewModel @Inject constructor(
    private val activityFilterUtils: ActivityFilterUtils,
    private val fileRepository: FileRepository,
    private val imageSizeUtils: ImageSizeUtils,
    private val visualizationUtils: VisualizationUtils,
    activitiesFromCacheUseCase: GetActivitiesFromCacheUseCase,
    gson: Gson,
    ssh: SavedStateHandle,
) : BaseRoutingViewModel<SaveArtViewState, SaveArtViewEvent, MainDestination>() {

    companion object {
        private const val PREVIEW_BITMAP_MAX_SIZE_WIDTH_PX = 2000
        private const val PREVIEW_BITMAP_MAX_SIZE_HEIGHT_PX = 2000
    }

    private val activityTypes = gson.fromJson<List<String>>(
        ActivityTypes.rawArg(ssh),
        List::class.java
    )
    private val activities = activitiesFromCacheUseCase()
    private val colorActivitiesArgb = ColorActivitiesArgb.rawArg(ssh).toInt()
    private val colorBackgroundArgb = ColorBackgroundArgb.rawArg(ssh).toInt()
    private val colorFontArgb = ColorFontArgb.rawArg(ssh).toInt()
    private val filterDateAfterMs = FilterDateAfterMs.rawArg(ssh).toLong()
    private val filterDateBeforeMs = FilterDateBeforeMs.rawArg(ssh).toLong()
    private val filterDistanceLessThan = FilterDistanceLessThan.rawArg(ssh).toDouble()
    private val filterDistanceMoreThan = FilterDistanceMoreThan.rawArg(ssh).toDouble()
    private val sizeHeightPx = SizeHeightPx.rawArg(ssh).toInt()
    private val sizeWidthPx = SizeWidthPx.rawArg(ssh).toInt()
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
                fileRepository
                    .saveBitmapToGallery(bitmapDownloadSize)
                    .doOnSuccess {
                        snackbarHostState.showSnackbar("Downloaded successfully")
                    }
                    .doOnError {
                        println("Error was $exception")
                        snackbarHostState.showSnackbar("Download failed")
                    }
                    .also { copyDownloadTerminate().push() }
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
            copyShareStart().push()
            viewModelScope.launch(Dispatchers.IO) {
                fileRepository
                    .saveBitmapToCache(bitmapDownloadSize)
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
            val secondsAfter = TimeUnit.MILLISECONDS.toSeconds(filterDateAfterMs)
            val secondsBefore = TimeUnit.MILLISECONDS.toSeconds(filterDateBeforeMs)
            val bitmap = visualizationUtils.createBitmap(
                activities = activityFilterUtils.filterActivities(
                    activities = activities,
                    includeActivityTypes = activityTypes.toSet(),
                    unixSecondsRange = secondsAfter..secondsBefore,
                    distanceRange = filterDistanceMoreThan..filterDistanceLessThan
                ),
                fontAssetPath = fontAssetPath,
                fontSize = fontTypeSize,
                colorActivitiesArgb = colorActivitiesArgb,
                colorBackgroundArgb = colorBackgroundArgb,
                colorFontArgb = colorFontArgb,
                bitmapSize = imageSizeUtils.sizeToMaximumSize(
                    actualSize = Size(sizeWidthPx, sizeHeightPx),
                    maximumSize = Size(
                        PREVIEW_BITMAP_MAX_SIZE_WIDTH_PX,
                        PREVIEW_BITMAP_MAX_SIZE_HEIGHT_PX
                    )
                ),
                strokeWidth = strokeWidthType,
                textLeft = textLeft,
                textCenter = textCenter,
                textRight = textRight,
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