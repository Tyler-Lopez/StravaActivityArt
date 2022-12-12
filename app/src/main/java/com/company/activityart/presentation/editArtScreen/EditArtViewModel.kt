package com.company.activityart.presentation.editArtScreen

import android.graphics.Bitmap
import android.util.Size
import androidx.compose.foundation.ScrollState
import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.models.ResolutionListFactory
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.NavigateUp
import com.company.activityart.presentation.editArtScreen.ColorType.*
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent.*
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.*
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.FilterDateChanged.*
import com.company.activityart.presentation.editArtScreen.EditArtViewState.Loading
import com.company.activityart.presentation.editArtScreen.EditArtViewState.Standby
import com.company.activityart.presentation.editArtScreen.StrokeWidthType.MEDIUM
import com.company.activityart.presentation.editArtScreen.StyleType.ACTIVITIES
import com.company.activityart.presentation.editArtScreen.StyleType.BACKGROUND
import com.company.activityart.util.ImageSizeUtils
import com.company.activityart.util.TimeUtils
import com.company.activityart.util.VisualizationUtils
import com.company.activityart.util.classes.YearMonthDay
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagerApi::class)
@HiltViewModel
class EditArtViewModel @Inject constructor(
    private val activitiesFromCacheUseCase: GetActivitiesFromCacheUseCase,
    private val imageSizeUtils: ImageSizeUtils,
    private val timeUtils: TimeUtils,
    private val resolutionListFactory: ResolutionListFactory,
    private val visualizationUtils: VisualizationUtils
) : BaseRoutingViewModel<EditArtViewState, EditArtViewEvent, MainDestination>() {

    companion object {
        private const val CUSTOM_SIZE_MINIMUM_PX = 100
        private const val CUSTOM_SIZE_MAXIMUM_PX = 12000
        private const val DEFAULT_ACTIVITY_TYPE_SELECTION = true
        private const val FADE_LENGTH_MS = 1000
        private const val INITIAL_ACTIVITIES_ALPHA = 1f
        private const val INITIAL_ACTIVITIES_BLUE = 1f
        private const val INITIAL_ACTIVITIES_GREEN = 1f
        private const val INITIAL_ACTIVITIES_RED = 1f
        private const val INITIAL_BACKGROUND_ALPHA = 1f
        private const val INITIAL_BACKGROUND_BLUE = 0f
        private const val INITIAL_BACKGROUND_GREEN = 0f
        private const val INITIAL_BACKGROUND_RED = 0f
        private const val INITIAL_HEIGHT_PX = 1080
        private const val INITIAL_WIDTH_PX = 1920
        private const val INITIAL_SCROLL_STATE = 0
        private const val INITIAL_SELECTED_RES_INDEX = 0
    }

    private val activities by lazy { activitiesFromCacheUseCase() }
    private val activitiesFiltered: List<Activity>
        get() {
            return activities.filter {
                withLastState {
                    val unixMs = timeUtils.iso8601StringToUnixMillisecond(it.iso8601LocalDate)
                    val unixSecondAfter = filterDateMinDateSelectedYearMonthDay.unixMsFirst
                    val unixSecondBefore = filterDateMaxDateSelectedYearMonthDay.unixMsLast
                    if (unixMs !in unixSecondAfter..unixSecondBefore) return@filter false
                    if (activitiesTypesSelectionMap[it.type] != true) return@filter false
                }
                true
            }
        }
    private val activitiesTypesSelectionMap by lazy {
        activities
            .map { it.type }
            .associateWith { DEFAULT_ACTIVITY_TYPE_SELECTION }
            .toMutableMap()
    }
    private val activitiesUnixMsList by lazy {
        activities.map {
            TimeUnit.SECONDS.toMillis(timeUtils.iso8601StringToUnixSecond(it.iso8601LocalDate))
        }.sorted()
    }
    private var bitmapFullSize: Bitmap? = null
    private val imageProcessingDispatcher by lazy { Dispatchers.Default.limitedParallelism(1) }
    private val pagerHeaders: List<EditArtHeaderType> = EditArtHeaderType.values().toList()
    private val pagerState = PagerState(pagerHeaders.size)
    private val pagerStateWrapper = PagerStateWrapper(
        pagerHeaders = pagerHeaders,
        pagerState = pagerState,
        fadeLengthMs = FADE_LENGTH_MS
    )
    private lateinit var screenSize: Size
    private val resolutionList by lazy {
        resolutionListFactory.create().apply { add(Resolution.CustomResolution()) }
    }
    private val customResolution by lazy {
        resolutionList.last() as? Resolution.CustomResolution ?: error("Missing custom resolution")
    }

    init {
        pushState(Loading(pagerStateWrapper = pagerStateWrapper, dialogNavigateUpActive = false))
    }

    override fun onEvent(event: EditArtViewEvent) {
        when (event) {
            is ArtMutatingEvent -> onArtMutatingEvent(event)
            is DialogNavigateUpCancelled -> onDialogNavigateUpCancelled()
            is DialogNavigateUpConfirmed -> onDialogNavigateUpConfirmed()
            is MakeFullscreenClicked -> onMakeFullscreenClicked()
            is NavigateUpClicked -> onNavigateUpClicked()
            is SaveClicked -> onSaveClicked()
            is SizeCustomChanged -> onSizeCustomChanged(event)
            is PageHeaderClicked -> onPageHeaderClicked(event)
        }
    }

    private fun onArtMutatingEvent(event: ArtMutatingEvent) {
        when (event) {
            is FilterDateChanged -> onFilterDateChanged(event)
            is FilterTypeToggled -> onFilterTypeToggled(event)
            is ScreenMeasured -> onScreenMeasured(event)
            is SizeChanged -> onSizeChanged(event)
            is SizeCustomChangeDone -> onSizeCustomChangeDone()
            is SizeRotated -> onSizeRotated(event)
            is StylesColorChanged -> onStylesColorChanged(event)
            is StylesStrokeWidthChanged -> onStylesStrokeWidthChanged(event)
        }
        updateBitmap()
    }

    private fun onDialogNavigateUpCancelled() {
        copyLastState { copy(dialogNavigateUpActive = false) }.push()
    }

    private fun onDialogNavigateUpConfirmed() {
        copyLastState { copy(dialogNavigateUpActive = false) }.push()
        viewModelScope.launch { routeTo(NavigateUp) }
    }

    private fun onFilterDateChanged(event: FilterDateChanged) {
        (lastPushedState as? Standby)?.run {
            when (event) {
                is FilterAfterChanged -> {
                    val maxUnixMs = filterDateMaxDateSelectedYearMonthDay.unixMs
                    val correctedUnixMs = event.changedTo.unixMs.coerceAtMost(maxUnixMs)
                    val correctedDate = YearMonthDay.fromUnixMs(correctedUnixMs)
                    copy(filterDateMinDateSelectedYearMonthDay = correctedDate)
                }
                is FilterBeforeChanged -> {
                    val minUnixMs = filterDateMinDateSelectedYearMonthDay.unixMs
                    val correctedUnixMs = event.changedTo.unixMs.coerceAtLeast(minUnixMs)
                    val correctedDate = YearMonthDay.fromUnixMs(correctedUnixMs)
                    copy(filterDateMaxDateSelectedYearMonthDay = correctedDate)
                }
            }
        }?.push()
    }

    private fun onFilterTypeToggled(event: FilterTypeToggled) {
        activitiesTypesSelectionMap.let {
            it[event.type] = !(it[event.type] ?: false)
        }
        copyLastState { copy(bitmap = null) }.push()
    }

    private fun onMakeFullscreenClicked() {

    }

    private fun onNavigateUpClicked() {
        copyLastState { copy(dialogNavigateUpActive = true) }.push()
    }

    private fun onPageHeaderClicked(event: PageHeaderClicked) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            (lastPushedState as? Standby)?.run {
                pagerStateWrapper.pagerState.scrollToPage(event.position)
            }
        }
    }

    private fun onSaveClicked() {
        viewModelScope.launch {
            (lastPushedState as? Standby)?.run {
                routeTo(MainDestination.NavigateSaveArt(bitmap!!))
            }
        }
    }

    private fun onScreenMeasured(event: ScreenMeasured) {
        val sizeActual = Size(INITIAL_WIDTH_PX, INITIAL_HEIGHT_PX)
        screenSize = Size(event.width, event.height)
        pushState(
            Standby(
                bitmap = null,
                dialogNavigateUpActive = false,
                filterDateMaxDateSelectedYearMonthDay = YearMonthDay.fromUnixMs(activitiesUnixMsList.last()),
                filterDateMinDateSelectedYearMonthDay = YearMonthDay.fromUnixMs(activitiesUnixMsList.first()),
                filterDateMaxDateTotalYearMonthDay = YearMonthDay.fromUnixMs(activitiesUnixMsList.last()),
                filterDateMinDateTotalYearMonthDay = YearMonthDay.fromUnixMs(activitiesUnixMsList.first()),
                filterDateSelectedActivitiesCount = 0, // todo
                filterTypesWithSelections = activitiesTypesSelectionMap,
                filterTypesCount = 0, // todo
                pagerStateWrapper = pagerStateWrapper,
                scrollStateFilter = ScrollState(INITIAL_SCROLL_STATE),
                scrollStateResize = ScrollState(INITIAL_SCROLL_STATE),
                scrollStateStyle = ScrollState(INITIAL_SCROLL_STATE),
                sizeActual = sizeActual,
                sizeCustomHeightPx = customResolution.heightPx,
                sizeCustomWidthPx = customResolution.widthPx,
                sizeCustomRangePx = CUSTOM_SIZE_MINIMUM_PX..CUSTOM_SIZE_MAXIMUM_PX,
                sizeResolutionList = resolutionList,
                sizeResolutionListSelectedIndex = INITIAL_SELECTED_RES_INDEX,
                styleActivities = ColorWrapper(
                    INITIAL_ACTIVITIES_ALPHA,
                    INITIAL_ACTIVITIES_BLUE,
                    INITIAL_ACTIVITIES_GREEN,
                    INITIAL_ACTIVITIES_RED
                ),
                styleBackground = ColorWrapper(
                    INITIAL_BACKGROUND_ALPHA,
                    INITIAL_BACKGROUND_BLUE,
                    INITIAL_BACKGROUND_GREEN,
                    INITIAL_BACKGROUND_RED
                ),
                styleStrokeWidthType = MEDIUM,
            )
        )
    }

    private fun onSizeChanged(event: SizeChanged) {
        val newSizeActual = resolutionList[event.changedIndex].run { Size(widthPx, heightPx) }
        copyLastState {
            copy(
                sizeActual = newSizeActual,
                sizeResolutionListSelectedIndex = event.changedIndex
            )
        }.push()
    }

    private fun onSizeCustomChangeDone() {
        copyLastState {
            resolutionList[resolutionList.lastIndex] = customResolution.copy(
                customWidthPx = sizeCustomWidthPx,
                customHeightPx = sizeCustomHeightPx
            )
            copy(bitmap = null)
        }.push()
    }

    private fun onSizeCustomChanged(event: SizeCustomChanged) {
        copyLastState {
            when (event) {
                is SizeCustomChanged.HeightChanged -> copy(sizeCustomHeightPx = event.changedToPx)
                is SizeCustomChanged.WidthChanged -> copy(sizeCustomWidthPx = event.changedToPx)
            }
        }.push()
    }

    private fun onSizeRotated(event: SizeRotated) {
        event.apply {
            resolutionList[rotatedIndex] =
                (resolutionList[rotatedIndex] as Resolution.RotatingResolution).copyWithRotation()
        }
        // Todo, believe this makes update immediate, kind of awkward looking hack, fix later
        copyLastState {
            copy(bitmap = null)
        }.push()
    }

    private fun onStylesColorChanged(event: StylesColorChanged) {
        copyLastState {
            event.run {
                when (styleType) {
                    BACKGROUND -> copy(styleBackground = styleBackground.copyWithEvent(event))
                    ACTIVITIES -> copy(styleActivities = styleActivities.copyWithEvent(event))
                }
            }
        }.push()
    }

    private fun onStylesStrokeWidthChanged(event: StylesStrokeWidthChanged) {
        (lastPushedState as? Standby)?.run {
            copy(styleStrokeWidthType = event.changedTo)
        }?.push()
    }

    /** @return Copy of an reflecting a [StylesColorChanged] change event
     *  to a [ColorWrapper]. **/
    private fun ColorWrapper.copyWithEvent(event: StylesColorChanged): ColorWrapper {
        return event.let {
            when (it.colorType) {
                ALPHA -> copy(alpha = it.changedTo)
                BLUE -> copy(blue = it.changedTo)
                GREEN -> copy(green = it.changedTo)
                RED -> copy(red = it.changedTo)
            }
        }
    }

    private fun updateBitmap() {
        imageProcessingDispatcher.cancelChildren()
        viewModelScope.launch(imageProcessingDispatcher) {
            copyLastState {
                val bitmap = visualizationUtils.createBitmap(
                    activities = activitiesFiltered,
                    colorActivities = styleActivities,
                    colorBackground = styleBackground,
                    paddingFraction = 0.05f,
                    strokeWidthType = styleStrokeWidthType,
                    bitmapSize = imageSizeUtils.sizeToMaximumSize(
                        actualSize = sizeResolutionList[sizeResolutionListSelectedIndex].run {
                            Size(widthPx, heightPx)
                        },
                        maximumSize = screenSize
                    )
                )
                copyLastState { copy(bitmap = bitmap) }
            }.push()
        }
    }

    private inline fun copyLastState(block: Standby.() -> Standby): Standby {
        return (lastPushedState as? Standby)?.run(block) ?: error("Last state was not standby")
    }

    private inline fun withLastState(block: Standby.() -> Unit) {
        (lastPushedState as? Standby)?.run(block)
    }

}