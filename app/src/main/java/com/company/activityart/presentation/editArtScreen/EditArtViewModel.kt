package com.company.activityart.presentation.editArtScreen

import android.util.Size
import androidx.compose.foundation.ScrollState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.domain.models.Activity
import com.company.activityart.domain.models.ResolutionListFactory
import com.company.activityart.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.NavigateSaveArt
import com.company.activityart.presentation.MainDestination.NavigateUp
import com.company.activityart.presentation.editArtScreen.ColorType.*
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent.*
import com.company.activityart.presentation.editArtScreen.EditArtFilterType.*
import com.company.activityart.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.*
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
import java.util.concurrent.TimeUnit.*
import java.util.logging.Filter
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
        private const val PREVIEW_BITMAP_MAX_SIZE_WIDTH_PX = 2000
        private const val PREVIEW_BITMAP_MAX_SIZE_HEIGHT_PX = 2000
    }

    private lateinit var activities: List<Activity>
    private lateinit var activitiesFilteredByFilterType: MutableMap<EditArtFilterType, List<Activity>>
    private var activitiesTypesSelectionsMap: Map<String, Boolean> = mapOf()
    private lateinit var activitiesDistancesList: List<Double>
    private lateinit var activitiesUnixMsList: List<Long>

    /** Updates [activitiesFilteredByFilterType] for a given [EditArtFilterType].
     * Designates which activities this particular filter type is in-charge of filtering. **/
    private fun EditArtFilterType.updateFilteredActivities() {
        val prevActivities = activitiesFilteredByFilterType[lastFilter] ?: activities
        val filteredActivities = prevActivities.filter { activity ->
            (lastPushedState as? Standby)?.run {
                when (this@updateFilteredActivities) {
                    DATE -> {
                        val unixMs =
                            timeUtils.iso8601StringToUnixMillisecond(activity.iso8601LocalDate)
                        val unixSecondAfter =
                            filterDateMinDateSelectedYearMonthDay?.unixMsFirst ?: Long.MIN_VALUE
                        val unixSecondBefore =
                            filterDateMaxDateSelectedYearMonthDay?.unixMsLast ?: Long.MAX_VALUE
                        unixMs in unixSecondAfter..unixSecondBefore
                    }
                    TYPE -> activitiesTypesSelectionsMap[activity.type]
                    DISTANCE -> filterDistanceSelected?.let {
                        activity.distance in it
                    } ?: true
                }
            } ?: true
        }
        activitiesFilteredByFilterType[this] = filteredActivities
    }

    /** Sets [activitiesTypesSelectionsMap], [activitiesUnixMsList]: the filters which the
     * user may control. Must not be invoked before [activities] is populated. **/
    private fun EditArtFilterType.updateFilters() {
        val filteredActivities = (activitiesFilteredByFilterType[lastFilter] ?: activities)
        when (this) {
            DATE -> activitiesUnixMsList = filteredActivities
                .map { SECONDS.toMillis(timeUtils.iso8601StringToUnixSecond(it.iso8601LocalDate)) }
            TYPE -> activitiesTypesSelectionsMap = filteredActivities
                .distinctBy { it.type }
                .map { it.type }
                .associateWith {
                    activitiesTypesSelectionsMap[it] ?: DEFAULT_ACTIVITY_TYPE_SELECTION
                }
            DISTANCE -> activitiesDistancesList = filteredActivities
                .map { it.distance }
        }
    }

    /** Pushes updated filter information as set in [updateFilters] to the View **/
    private fun EditArtFilterType.pushUpdatedFiltersToView() {
        copyLastState {
            when (this@pushUpdatedFiltersToView) {
                DATE -> {

                    val activitiesFirstSecond = activitiesUnixMsList.minOrNull()
                    val activitiesLastSecond = activitiesUnixMsList.maxOrNull()

                    copy(
                        filterDateMaxDateSelectedYearMonthDay = filterDateMaxDateSelectedYearMonthDay?.run {
                            unixMsLast
                                .takeIf { it <= (activitiesLastSecond ?: Long.MIN_VALUE) }
                                ?.let { YearMonthDay.fromUnixMs(it) }
                        },
                        filterDateMinDateSelectedYearMonthDay = filterDateMinDateSelectedYearMonthDay?.run {
                            unixMsFirst
                                .takeIf { it >= (activitiesFirstSecond ?: Long.MAX_VALUE) }
                                ?.let { YearMonthDay.fromUnixMs(it) }
                        },
                        filterDateMaxDateTotalYearMonthDay = activitiesLastSecond?.let {
                            YearMonthDay.fromUnixMs(it)
                        },
                        filterDateMinDateTotalYearMonthDay = activitiesFirstSecond?.let {
                            YearMonthDay.fromUnixMs(it)
                        }
                    )
                }
                TYPE -> copy(filterTypesWithSelections = activitiesTypesSelectionsMap.toList())
                DISTANCE -> {
                    val distanceShortest = activitiesDistancesList.minOrNull()
                    val distanceLongest = activitiesDistancesList.maxOrNull()

                    copy(
                        filterDistanceSelected = filterDistanceSelected?.run {
                            val adjStart = start
                                .takeIf { it >= (distanceShortest ?: Double.MAX_VALUE) }
                            val adjEnd = endInclusive
                                .takeIf { it <= (distanceLongest ?: Double.MIN_VALUE) }
                            adjEnd?.let { adjStart?.rangeTo(it) }
                        },
                        filterDistanceTotal = distanceLongest?.let { distanceShortest?.rangeTo(it) }
                    )
                }
            }
        }.push()
    }

    private val imageProcessingDispatcher by lazy { Dispatchers.Default.limitedParallelism(1) }
    private val pagerHeaders: List<EditArtHeaderType> = EditArtHeaderType.values().toList()
    private val pagerState = PagerState(pagerHeaders.size)
    private val pagerStateWrapper = PagerStateWrapper(
        pagerHeaders = pagerHeaders,
        pagerState = pagerState,
        fadeLengthMs = FADE_LENGTH_MS
    )
    private val resolutionList by lazy {
        resolutionListFactory.create().apply { add(Resolution.CustomResolution()) }
    }
    private val customResolution by lazy {
        resolutionList.last() as? Resolution.CustomResolution ?: error("Missing custom resolution")
    }

    init {
        Loading(pagerStateWrapper = pagerStateWrapper, dialogNavigateUpActive = false).push()
        viewModelScope.launch {
            activities = activitiesFromCacheUseCase()

            EditArtFilterType.values().apply {
                activitiesFilteredByFilterType = associateWith { activities }.toMutableMap()
                forEach { it.updateFilters() }
            }

            Standby(
                bitmap = null,
                dialogNavigateUpActive = false,
                filterDateMaxDateSelectedYearMonthDay = null,
                filterDateMinDateSelectedYearMonthDay = null,
                filterDateMaxDateTotalYearMonthDay = YearMonthDay.fromUnixMs(activitiesUnixMsList.max()),
                filterDateMinDateTotalYearMonthDay = YearMonthDay.fromUnixMs(activitiesUnixMsList.min()),
                filterDateSelectedActivitiesCount = 0, // todo
                filterDistanceSelected = null,
                filterDistanceTotal = activitiesDistancesList
                    .takeIf { it.isNotEmpty() }
                    ?.run { min()..max() },
                filterTypesWithSelections = activitiesTypesSelectionsMap.toList(),
                filterTypesCount = 0, // todo
                pagerStateWrapper = pagerStateWrapper,
                scrollStateFilter = ScrollState(INITIAL_SCROLL_STATE),
                scrollStateResize = ScrollState(INITIAL_SCROLL_STATE),
                scrollStateStyle = ScrollState(INITIAL_SCROLL_STATE),
                sizeActual = Size(INITIAL_WIDTH_PX, INITIAL_HEIGHT_PX),
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
            ).push()
            updateBitmap()
        }
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
            is FilterChanged -> onFilterChangeEvent(event)
            is SizeChanged -> onSizeChanged(event)
            is SizeCustomChangeDone -> onSizeCustomChangeDone()
            is SizeRotated -> onSizeRotated(event)
            is StylesColorChanged -> onStylesColorChanged(event)
            is StylesStrokeWidthChanged -> onStylesStrokeWidthChanged(event)
        }
        updateBitmap()
    }

    private fun onFilterChangeEvent(event: FilterChanged) {
        when (event) {
            is FilterChanged.FilterDateChanged -> onFilterDateChanged(event)
            is FilterChanged.FilterDistanceChanged -> onFilterDistanceChanged(event)
            is FilterChanged.FilterTypeToggled -> onFilterTypeToggled(event)
        }
        event.filterType.apply {
            updateFilteredActivities()
            forEachNextFilterType {
                it.updateFilteredActivities()
                it.updateFilters()
                it.pushUpdatedFiltersToView()
            }
        }
    }

    private fun onDialogNavigateUpCancelled() {
        copyLastState { copy(dialogNavigateUpActive = false) }.push()
    }

    private fun onDialogNavigateUpConfirmed() {
        copyLastState { copy(dialogNavigateUpActive = false) }.push()
        viewModelScope.launch { routeTo(NavigateUp) }
    }

    private fun onFilterDateChanged(event: FilterChanged.FilterDateChanged) {
        (lastPushedState as? Standby)?.run {
            when (event) {
                is FilterChanged.FilterDateChanged.FilterAfterChanged -> {
                    val maxUnixMs = activitiesUnixMsList.max()
                    val correctedUnixMs = event.changedTo.unixMs.coerceAtMost(maxUnixMs)
                    val correctedDate = YearMonthDay.fromUnixMs(correctedUnixMs)
                    copy(filterDateMinDateSelectedYearMonthDay = correctedDate)
                }
                is FilterChanged.FilterDateChanged.FilterBeforeChanged -> {
                    val minUnixMs = activitiesUnixMsList.min()
                    val correctedUnixMs = event.changedTo.unixMs.coerceAtLeast(minUnixMs)
                    val correctedDate = YearMonthDay.fromUnixMs(correctedUnixMs)
                    copy(filterDateMaxDateSelectedYearMonthDay = correctedDate)
                }
            }
        }?.push()
    }

    private fun onFilterDistanceChanged(event: FilterChanged.FilterDistanceChanged) {
        copyLastState {
            copy(filterDistanceSelected = event.changedTo)
        }.push()
    }

    private fun onFilterTypeToggled(event: FilterChanged.FilterTypeToggled) {
        activitiesTypesSelectionsMap = activitiesTypesSelectionsMap
            .toMutableMap()
            .apply {
                set(event.type, !get(event.type)!!)
            }
        copyLastState {
            copy(filterTypesWithSelections = activitiesTypesSelectionsMap.toList())
        }.push()
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
            // Todo, determine how to handle when no activities are selected
            (lastPushedState as? Standby)?.run {
                val targetSize = sizeResolutionList[sizeResolutionListSelectedIndex]
                routeTo(
                    NavigateSaveArt(
                        activityTypes = filterTypesWithSelections
                            .filter { it.second }
                            .map { it.first },
                        colorActivitiesArgb = styleActivities.color.toArgb(),
                        colorBackgroundArgb = styleBackground.color.toArgb(),
                        filterBeforeMs = filterDateMinDateSelectedYearMonthDay?.unixMsLast
                            ?: Long.MAX_VALUE,
                        filterAfterMs = filterDateMinDateSelectedYearMonthDay?.unixMsFirst
                            ?: Long.MIN_VALUE,
                        sizeHeightPx = targetSize.heightPx,
                        sizeWidthPx = targetSize.widthPx,
                        strokeWidthType = styleStrokeWidthType
                    )
                )
            }
        }
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
                    activities = activitiesFilteredByFilterType[EditArtFilterType.filterFinal]!!,
                    colorActivitiesArgb = styleActivities.color.toArgb(),
                    colorBackgroundArgb = styleBackground.color.toArgb(),
                    strokeWidthType = styleStrokeWidthType,
                    bitmapSize = imageSizeUtils.sizeToMaximumSize(
                        actualSize = sizeResolutionList[sizeResolutionListSelectedIndex].run {
                            Size(widthPx, heightPx)
                        },
                        maximumSize = Size(
                            PREVIEW_BITMAP_MAX_SIZE_WIDTH_PX,
                            PREVIEW_BITMAP_MAX_SIZE_HEIGHT_PX
                        )
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