package com.activityartapp.presentation.saveArtScreen

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import android.util.Size
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.repository.FileRepository
import com.activityartapp.domain.useCase.activities.GetActivitiesFromDisk
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.MainDestination.*
import com.activityartapp.presentation.editArtScreen.StrokeWidthType
import com.activityartapp.presentation.errorScreen.ErrorScreenType
import com.activityartapp.presentation.saveArtScreen.SaveArtViewState.*
import com.activityartapp.presentation.saveArtScreen.SaveArtViewState.Standby.DownloadShareStatusType
import com.activityartapp.presentation.saveArtScreen.SaveArtViewState.Standby.DownloadShareStatusType.*
import com.activityartapp.presentation.saveArtScreen.SaveArtViewEvent.*
import com.activityartapp.util.*
import com.activityartapp.util.NavArgSpecification.*
import com.activityartapp.util.enums.*
import com.activityartapp.util.enums.BackgroundType
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
    private val getActivitiesFromDisk: GetActivitiesFromDisk,
    gson: Gson,
    ssh: SavedStateHandle,
) : BaseRoutingViewModel<SaveArtViewState, SaveArtViewEvent, MainDestination>() {

    private val activityTypes = gson
        .fromJson<List<String>>(
            ActivityTypesArg.rawArg(ssh),
            List::class.java
        )
        .map { SportType.valueOf(it) }
    private lateinit var activities: List<Activity>
    private val athleteId = AthleteIdArg.rawArg(ssh).toLong()
    private val backgroundGradientAngleType =
        GradientAngleType.valueOf(BackgroundGradientAngleTypeArg.rawArg(ssh))
    private val backgroundType = BackgroundType.valueOf(BackgroundTypeArg.rawArg(ssh))
    private val colorActivitiesArgb = ColorActivitiesArgbArg.rawArg(ssh).toInt()
    private val colorBackgroundArgbList = gson.fromJson<List<String>>(
        ColorBackgroundArgbListArg.rawArg(ssh),
        List::class.java
    ).map { it.toInt() }
    private val colorFontArgb = ColorFontArgbArg.rawArg(ssh).toInt()
    private val filterDateAfterMs = FilterDateAfterMsArg.rawArg(ssh).toLong()
    private val filterDateBeforeMs = FilterDateBeforeMsArg.rawArg(ssh).toLong()
    private val filterDistanceLessThanMeters = FilterDistanceLessThanMetersArg.rawArg(ssh).toInt()
    private val filterDistanceMoreThanMeters = FilterDistanceMoreThanMetersArg.rawArg(ssh).toInt()
    private val sizeHeightPx = SizeHeightPxArg.rawArg(ssh).toInt()
    private val sizeWidthPx = SizeWidthPxArg.rawArg(ssh).toInt()
    private val sizePx: Size get() = Size(sizeWidthPx, sizeHeightPx)
    private val sortDirectionType =
        EditArtSortDirectionType.valueOf(SortDirectionTypeArg.rawArg(ssh))
    private val sortType = EditArtSortType.valueOf(SortTypeArg.rawArg(ssh))
    private val strokeWidthType = StrokeWidthType.valueOf(StrokeWidthArg.rawArg(ssh))
    private val textLeft = TextLeftArg.rawArg(ssh).takeIf { it.isNotBlank() }
    private val textCenter = TextCenterArg.rawArg(ssh).takeIf { it.isNotBlank() }
    private val textRight = TextRightArg.rawArg(ssh).takeIf { it.isNotBlank() }
    private val fontAssetPath = TextFontAssetPathArg.rawArg(ssh)
    private val fontTypeSize = FontSizeType.valueOf(TextFontSizeArg.rawArg(ssh))

    override fun onEvent(event: SaveArtViewEvent) {
        when (event) {
            is ActivityResumed -> onActivityResumed()
            is ClickedDownload -> onClickedDownload()
            is ClickedDownloadWhenPermissionPermaDenied -> onClickedDownloadWhenPermissionPermaDenied()
            is ClickedNavigateUp -> onClickedNavigateUp()
            is ClickedShare -> onClickedShare()
            is ReceivedDownloadFailure -> onReceivedDownloadFailure()
            is ReceivedDownloadSuccess -> onReceivedDownloadSuccess()
            is ReceivedShareFailure -> onReceivedShareFailure()
            is ReceivedShareSuccess -> onReceivedShareSuccess()
            is ScreenMeasured -> onScreenMeasured(event)
        }
    }

    private fun onActivityResumed() {
        pushUpdateToDownloadShareStatus(STANDBY)
    }

    private fun onClickedDownload() {
        pushUpdateToDownloadShareStatus(DOWNLOAD_IN_PROGRESS)
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.Default) {
                fileRepository
                    .saveBitmapToGallery(
                        bitmap = createArtBitmapOfSize(
                            isPreview = false,
                            size = sizePx
                        ),
                        withTransparency = backgroundType == BackgroundType.TRANSPARENT
                    )
                    .doOnSuccess { pushUpdateToDownloadShareStatus(DOWNLOAD_SUCCESS) }
                    .doOnError { pushUpdateToDownloadShareStatus(DOWNLOAD_FAILURE) }
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
        pushUpdateToDownloadShareStatus(SHARE_IN_PROGRESS)
        viewModelScope.launch(Dispatchers.IO) {
            fileRepository
                .saveBitmapToCache(
                    bitmap = createArtBitmapOfSize(
                        isPreview = false,
                        size = sizePx
                    ),
                    withTransparency = backgroundType == BackgroundType.TRANSPARENT
                )
                .doOnSuccess {
                    withContext(Dispatchers.Main) {
                        routeTo(ShareFile(data))
                    }
                    pushUpdateToDownloadShareStatus(SHARE_SUCCESS)
                }
                .doOnError { pushUpdateToDownloadShareStatus(SHARE_FAILURE) }
        }
    }

    private fun onReceivedDownloadFailure() {
        pushUpdateToDownloadShareStatus(STANDBY)
    }

    private fun onReceivedDownloadSuccess() {
        pushUpdateToDownloadShareStatus(STANDBY)
    }

    private fun onReceivedShareFailure() {
        pushUpdateToDownloadShareStatus(STANDBY)
    }

    private fun onReceivedShareSuccess() {
        pushUpdateToDownloadShareStatus(SHARE_IN_PROGRESS)
    }

    private fun onScreenMeasured(event: ScreenMeasured) {
        Loading.push()
        viewModelScope.launch(Dispatchers.Default) {
            activities = getActivitiesFromDisk(athleteId)
            Standby(
                bitmapScreenSize = createArtBitmapOfSize(
                    isPreview = true,
                    size = imageSizeUtils.sizeToMaximumSize(
                        actualSize = sizePx,
                        maximumSize = event.size
                    )
                )
            ).push()
        }
    }

    private fun createArtBitmapOfSize(isPreview: Boolean, size: Size): Bitmap {
        return visualizationUtils.createBitmap(
            activities = activityFilterUtils.filterActivities(
                activities = activities,
                includeActivityTypes = activityTypes.toSet(),
                unixMsRange = filterDateAfterMs..filterDateBeforeMs,
                distanceRange = filterDistanceMoreThanMeters..filterDistanceLessThanMeters
            ),
            backgroundGradientAngleType = backgroundGradientAngleType,
            backgroundType = backgroundType,
            fontAssetPath = fontAssetPath,
            fontSize = fontTypeSize,
            isPreview = isPreview,
            colorActivitiesArgb = colorActivitiesArgb,
            backgroundColorsArgb = colorBackgroundArgbList,
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

    private fun pushUpdateToDownloadShareStatus(downloadShareStatusType: DownloadShareStatusType) {
        (lastPushedState as? Standby)
            ?.copy(downloadShareStatusType = downloadShareStatusType)
            ?.push()
    }
}