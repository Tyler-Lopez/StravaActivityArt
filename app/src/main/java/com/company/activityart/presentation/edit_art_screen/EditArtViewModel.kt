package com.company.activityart.presentation.edit_art_screen

import android.util.Size
import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.models.ResolutionListFactory
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.NavigateUp
import com.company.activityart.presentation.edit_art_screen.ColorType.*
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.*
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.ArtMutatingEvent.*
import com.company.activityart.presentation.edit_art_screen.EditArtViewEvent.ArtMutatingEvent.FilterTypeChanged.FilterTypeAdded
import com.company.activityart.presentation.edit_art_screen.EditArtViewState.Loading
import com.company.activityart.presentation.edit_art_screen.EditArtViewState.Standby
import com.company.activityart.presentation.edit_art_screen.StrokeWidthType.MEDIUM
import com.company.activityart.presentation.edit_art_screen.StyleType.ACTIVITIES
import com.company.activityart.presentation.edit_art_screen.StyleType.BACKGROUND
import com.company.activityart.util.ImageSizeUtils
import com.company.activityart.util.TimeUtils
import com.company.activityart.util.VisualizationUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalPagerApi::class)
@HiltViewModel
class EditArtViewModel @Inject constructor(
    private val activitiesFromCacheUseCase: GetActivitiesFromCacheUseCase,
    private val imageSizeUtils: ImageSizeUtils,
    private val timeUtils: TimeUtils,
    private val resolutionListFactory: ResolutionListFactory,
    private val visualizationUtils: VisualizationUtils
) : BaseRoutingViewModel<EditArtViewState, EditArtViewEvent, MainDestination>() {

    companion object {
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
        private const val EDIT_SIZE_HEIGHT = 256
    }

    private val activities by lazy { activitiesFromCacheUseCase() }
    private val imageProcessingDispatcher by lazy { Dispatchers.Default.limitedParallelism(1) }
    private val pagerHeaders: List<EditArtHeaderType> = EditArtHeaderType.values().toList()
    private val pagerState = PagerState(pagerHeaders.size)
    private val pagerStateWrapper = PagerStateWrapper(
        pagerHeaders = pagerHeaders,
        pagerState = pagerState,
        fadeLengthMs = FADE_LENGTH_MS
    )
    private lateinit var screenSize: Size
    private val resolutionList by lazy { resolutionListFactory.create() }
    private val unixSeconds by lazy {
        activities.map {
            timeUtils.iso8601StringToUnixSecond(it.iso8601LocalDate)
        }
    }

    init {
        pushState(Loading(pagerStateWrapper = pagerStateWrapper))
    }

    override fun onEvent(event: EditArtViewEvent) {
        when (event) {
            is ArtMutatingEvent -> onArtMutatingEvent(event)
            is MakeFullscreenClicked -> onMakeFullscreenClicked()
            is NavigateUpClicked -> onNavigateUpClicked()
            is SaveClicked -> onSaveClicked()
            is PageHeaderClicked -> onPageHeaderClicked(event)
        }
    }

    private fun onArtMutatingEvent(event: ArtMutatingEvent) {
        when (event) {
            is FilterDateChanged -> onFilterDateChanged(event)
            is FilterTypeChanged -> onFilterTypeChanged(event)
            is ScreenMeasured -> onScreenMeasured(event)
            is SizeChanged -> onSizeChanged(event)
            is SizeRotated -> onSizeRotated(event)
            is StylesColorChanged -> onStylesColorChanged(event)
            is StylesStrokeWidthChanged -> onStylesStrokeWidthChanged(event)
        }
        updateBitmap()
    }

    private fun onFilterDateChanged(event: FilterDateChanged) {
        (lastPushedState as? Standby)?.run {
            copy(
                filterStateWrapper = filterStateWrapper.copy(
                    unixSecondSelectedStart = event.newUnixSecondStart,
                    unixSecondSelectedEnd = event.newUnixSecondEnd
                )
            )
        }?.push()
    }

    private fun onFilterTypeChanged(event: FilterTypeChanged) {
        (lastPushedState as? Standby)?.run {
            copy(
                filterStateWrapper = filterStateWrapper.copy(
                    excludedActivityTypes = filterStateWrapper
                        .excludedActivityTypes
                        .toMutableSet()
                        .apply {
                            if (event is FilterTypeAdded) {
                                add(event.type)
                            } else {
                                remove(event.type)
                            }
                        }
                )
            )
        }?.push()
    }

    private fun onMakeFullscreenClicked() {

    }

    private fun onNavigateUpClicked() {
        viewModelScope.launch(Dispatchers.Default) {
            routeTo(NavigateUp)
        }
    }

    private fun onPageHeaderClicked(event: PageHeaderClicked) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            (lastPushedState as? Standby)?.run {
                pagerStateWrapper.pagerState.scrollToPage(event.position)
            }
        }
    }

    private fun onSaveClicked() {

    }

    private fun onScreenMeasured(event: ScreenMeasured) {
        val sizeActual = Size(INITIAL_WIDTH_PX, INITIAL_HEIGHT_PX)
        screenSize = Size(event.width, event.height)
        pushState(
            Standby(
                bitmap = null,
                filterStateWrapper = FilterStateWrapper(
                    unixSecondSelectedStart = unixSeconds.first(),
                    unixSecondSelectedEnd = unixSeconds.last()
                ),
                pagerStateWrapper = pagerStateWrapper,
                sizeActual = sizeActual,
                sizeResolutionList = resolutionList,
                sizeResolutionListSelectedIndex = 0, // todo const
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
        val newSizeActual = resolutionList[event.selectedIndex].run { Size(widthPx, heightPx) }
        withLastState {
            copy(
                sizeActual = newSizeActual,
                sizeResolutionListSelectedIndex = event.selectedIndex
            ).push()
        }
    }

    private fun onSizeRotated(event: SizeRotated) {
            (resolutionList[event.rotatedIndex] as Resolution.SwappableResolution).apply {
                swapWidthWithHeight = !swapWidthWithHeight
            }
    }

    private fun onStylesColorChanged(event: StylesColorChanged) {
        withLastState {
            event.run {
                when (styleType) {
                    BACKGROUND -> copy(styleBackground = styleBackground.copyWithEvent(event))
                    ACTIVITIES -> copy(styleActivities = styleActivities.copyWithEvent(event))
                }
            }.push()
        }
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
            withLastState {
                val bitmap = visualizationUtils.createBitmap(
                    activities = activities,
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
                withLastState { copy(bitmap = bitmap).push() }
            }
        }
    }

    private inline fun withLastState(block: Standby.() -> Unit) {
        (lastPushedState as? Standby)?.run(block)
    }
}