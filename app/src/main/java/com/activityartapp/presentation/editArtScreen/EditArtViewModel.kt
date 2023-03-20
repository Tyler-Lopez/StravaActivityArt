package com.activityartapp.presentation.editArtScreen

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.useCase.activities.GetActivitiesFromDisk
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.MainDestination.NavigateUp
import com.activityartapp.presentation.editArtScreen.ColorType.*
import com.activityartapp.presentation.editArtScreen.EditArtFilterType.*
import com.activityartapp.presentation.editArtScreen.EditArtFilterType.Companion.filterFinal
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.*
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.*
import com.activityartapp.presentation.editArtScreen.EditArtViewState.Loading
import com.activityartapp.presentation.editArtScreen.EditArtViewState.Standby
import com.activityartapp.presentation.editArtScreen.subscreens.resize.ResolutionListFactoryImpl
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeSection
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeType
import com.activityartapp.util.*
import com.activityartapp.util.enums.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.math.roundToInt

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagerApi::class)
@HiltViewModel
@Stable
class EditArtViewModel @Inject constructor(
    private val getActivitiesFromDisk: GetActivitiesFromDisk,
    private val activityFilterUtils: ActivityFilterUtils,
    private val imageSizeUtils: ImageSizeUtils,
    private val timeUtils: TimeUtils,
    private val visualizationUtils: VisualizationUtils,
    private val parseNumberFromStringUtils: ParseNumberFromStringUtils,
    private val ssh: SavedStateHandle
) : BaseRoutingViewModel<EditArtViewState, EditArtViewEvent, MainDestination>() {

    companion object {
        private const val SSH_FILTER_ACTIVITIES_COUNT_DATE = "filterActivitiesCountDate"
        private const val SSH_FILTER_ACTIVITIES_COUNT_DISTANCE = "filterActivitiesCountDistance"
        private const val SSH_FILTER_ACTIVITIES_COUNT_TYPE = "filterActivitiesCountType"
        private const val SSH_FILTER_DATE_SELECTIONS = "filterDateSelections"
        private const val SSH_FILTER_DATE_SELECTION_INDEX = "filterDateSelectionIndex"
        private const val SSH_FILTER_DISTANCE_SELECTED_START = "filterDistanceSelectedStart"
        private const val SSH_FILTER_DISTANCE_SELECTED_END = "filterDistanceSelectedEnd"
        private const val SSH_FILTER_DISTANCE_TOTAL_START = "filterDistanceTotalStart"
        private const val SSH_FILTER_DISTANCE_TOTAL_END = "filterDistanceTotalEnd"
        private const val SSH_FILTER_TYPES = "filterTypes"
        private const val SSH_SIZE_RESOLUTION_LIST = "sizeResolutionList"
        private const val SSH_SIZE_RESOLUTION_LIST_SELECTED_INDEX =
            "sizeResolutionListSelectedIndex"
        private const val SSH_SORT_TYPE_SELECTED = "sortTypeSelected"
        private const val SSH_SORT_DIRECTION_TYPE_SELECTED = "sortDirectionTypeSelected"
        private const val SSH_STYLE_ACTIVITIES = "styleActivities"
        private const val SSH_STYLE_BACKGROUND_LIST = "styleBackgroundList"
        private const val SSH_STYLE_BACKGROUND_ANGLE_TYPE = "styleBackgroundAngleType"
        private const val SSH_STYLE_BACKGROUND_GRADIENT_COLOR_COUNT =
            "styleBackgroundGradientColorCount"
        private const val SSH_STYLE_BACKGROUND_TYPE = "styleBackgroundType"
        private const val SSH_STYLE_FONT = "styleFont"
        private const val SSH_STYLE_STROKE_WIDTH_TYPE = "strokeWidthType"
        private const val SSH_TYPE_ACTIVITIES_DISTANCE_METERS_SUMMED =
            "typeActivitiesDistanceMetersSummed"
        private const val SSH_TYPE_FONT_SELECTED = "typeFontSelected"
        private const val SSH_TYPE_FONT_WEIGHT_SELECTED = "typeFontWeightSelected"
        private const val SSH_TYPE_FONT_ITALICIZED = "typeFontItalicized"
        private const val SSH_TYPE_FONT_SIZE_SELECTED = "typeFontSizeSelected"
        private const val SSH_TYPE_LEFT_SELECTED = "typeLeftSelected"
        private const val SSH_TYPE_LEFT_CUSTOM_TEXT = "typeLeftCustomText"
        private const val SSH_TYPE_CENTER_SELECTED = "typeCenterSelected"
        private const val SSH_TYPE_CENTER_CUSTOM_TEXT = "typeCenterCustomText"
        private const val SSH_TYPE_RIGHT_SELECTED = "typeRightSelected"
        private const val SSH_TYPE_RIGHT_CUSTOM_TEXT = "typeRightCustomText"

        private const val INDEX_FIRST = 0
    }

    /** All activities stored on-disk **/
    private lateinit var activities: List<Activity>

    private val athleteId = NavArgSpecification.AthleteIdArg.rawArg(ssh).toLong()

    /** The list of activities for each [EditArtFilterType] **/
    private val activitiesFilteredByFilterType: MutableMap<EditArtFilterType, List<Activity>> =
        mutableMapOf()

    /** The activities which are drawn after all filters have been applied **/
    private val activitiesFiltered: List<Activity>
        get() = activitiesFilteredByFilterType[filterFinal] ?: activities

    /** Determine current start and end MS of selected Date filters  **/
    private val activitiesDateRangeUnixMs: LongProgression?
        get() = _filterDateSelections.let { selections ->
            val index = _filterDateSelectionIndex.value

            selections.getOrNull(index)?.run {
                when (this) {
                    is DateSelection.All -> Long.MIN_VALUE..Long.MAX_VALUE
                    is DateSelection.Year -> timeUtils
                        .firstUnixMsOfYear(year)
                        .rangeTo(timeUtils.lastUnixMsOfYear(year))
                    is DateSelection.Custom -> dateSelected
                }
            }
        }

    /** The total distance, in meters, of the filtered activities **/
    private val activitiesSummedDistance: Double get() = activitiesFiltered.sumOf { it.distance }

    /** Updates [activitiesFilteredByFilterType] for a given [EditArtFilterType].
     * Designates which activities this particular filter sportType is in-charge of filtering. **/
    private fun EditArtFilterType.updateFilteredActivities() {
        val prevActivities = activitiesFilteredByFilterType[lastFilter] ?: activities
        activitiesFilteredByFilterType[this@updateFilteredActivities] =
            prevActivities.filter { activity ->
                when (this@updateFilteredActivities) {
                    DATE -> activityFilterUtils.activityWithinUnixMs(
                        activity = activity,
                        range = activitiesDateRangeUnixMs ?: Long.MIN_VALUE..Long.MAX_VALUE
                    )
                    TYPE -> activityFilterUtils.activityTypeContainedWithinTypes(
                        activity = activity,
                        types = filteredTypes
                    )
                    DISTANCE -> activityFilterUtils.activityWithinDistanceRange(
                        activity = activity,
                        range = filteredDistanceRangeMeters
                    )
                }
            }
        pushUpdatedFilteredActivityCountToView()
    }

    /** For a given [EditArtFilterType], update filters the user may choose which
     * are encapsulated within [Standby] **/
    private fun EditArtFilterType.updateFilters() {
        val filteredActivities = (activitiesFilteredByFilterType[lastFilter] ?: activities)
        when (this@updateFilters) {
            DATE -> {
                var selectionIndex = INDEX_FIRST
                val selections = activityFilterUtils
                    .getPossibleDateSelections(
                        activities = filteredActivities,
                        customRangeSelectedPreviousMs = _filterDateSelections
                            .find { it is DateSelection.Custom }
                            ?.run { this as? DateSelection.Custom }
                            ?.run { dateSelected }
                    )
                    ?.also {
                        selectionIndex = _filterDateSelectionIndex.value
                            .takeIf { prevIndex -> prevIndex in INDEX_FIRST..it.lastIndex }
                            ?: INDEX_FIRST
                    }
                if (selections != null) {
                    val difference = selections.subtract(_filterDateSelections)
                    _filterDateSelections.removeIf { !selections.contains(it) }
                    _filterDateSelections.addAll(difference)
                } else {
                    _filterDateSelections.clear()
                }
                _filterDateSelectionIndex.value = selectionIndex
            }
            TYPE -> {
                val newFilterTypes = activityFilterUtils.getPossibleActivityTypes(
                    activities = filteredActivities,
                    filterTypesPrevious = _filterTypes.toMap() // todo
                )?.toMutableMap()

                if (newFilterTypes != null) {
                    val iterator = _filterTypes.iterator()
                    while (iterator.hasNext()) {
                        val filterType: Pair<SportType, Boolean> = iterator.next()
                        if (newFilterTypes.contains(filterType.first)) {
                            newFilterTypes.remove(filterType.first)
                        } else {
                            iterator.remove()
                        }
                    }
                    _filterTypes.addAll(newFilterTypes.toList())
                } else {
                    _filterTypes.clear()
                }
            }
            DISTANCE -> {
                val newRangeTotal = activityFilterUtils.getPossibleDistances(filteredActivities)
                val newRangeSelected = newRangeTotal?.let { range ->
                    val adjStart = _filterDistanceSelectedStart.value?.takeIf {
                        it >= range.start && it <= range.endInclusive
                    } ?: range.start
                    val adjEnd = _filterDistanceSelectedEnd.value?.takeIf {
                        it >= range.start && it <= range.endInclusive
                    } ?: range.endInclusive
                    adjStart..adjEnd
                }
                _filterDistancePendingChangeStart.value = null
                _filterDistancePendingChangeEnd.value = null
                _filterDistanceSelectedStart.value = newRangeSelected?.start
                _filterDistanceSelectedEnd.value = newRangeSelected?.endInclusive
                _filterDistanceTotalStart.value = newRangeTotal?.start
                _filterDistanceTotalEnd.value = newRangeTotal?.endInclusive
            }
        }
    }

    private val EditArtFilterType.activitiesCount: Int
        get() = activitiesFilteredByFilterType[this]?.size ?: 0

    private fun EditArtFilterType.pushUpdatedFilteredActivityCountToView() {
        when (this@pushUpdatedFilteredActivityCountToView) {
            DATE -> _filterActivitiesCountDate.value = DATE.activitiesCount
            DISTANCE -> _filterActivitiesCountDistance.value = DISTANCE.activitiesCount
            TYPE -> _filterActivitiesCountType.value = TYPE.activitiesCount
        }
        if (this@pushUpdatedFilteredActivityCountToView == filterFinal) {
            _typeActivitiesDistanceMetersSummed.value = activitiesSummedDistance.roundToInt()
        }
    }

    /** region States unobserved in View */
    private val activitiesProcessingDispatcher by lazy { Dispatchers.Default.limitedParallelism(1) }
    private var imageJob: Job? = null
    private var imageZoomedJob: Job? = null
    private val imageProcessingDispatcher by lazy { Dispatchers.Default.limitedParallelism(1) }
    private val imageZoomedProcessingDispatcher by lazy { Dispatchers.Default.limitedParallelism(1) }

    // The size of the screen excluding the top bar
    private var previewScreenSize: Size? = null
    /** endregion **/

    /** region States observed in View */
    // GLOBAL
    private val _dialogActive = mutableStateOf<EditArtDialog>(EditArtDialog.None) // not save

    // PREVIEW
    private val _bitmap = mutableStateOf<Bitmap?>(null) // not save
    private val _bitmapZoomed = mutableStateOf<Bitmap?>(null) // not save

    // FILTERS
    private val _filterActivitiesCountDate = mutableStateOf(0)
    private val _filterActivitiesCountDistance = mutableStateOf(0)
    private val _filterActivitiesCountType = mutableStateOf(0)
    private val _filterDateSelections = mutableStateListOf<DateSelection>()
    private val _filterDateSelectionIndex = mutableStateOf(0)
    private val _filterDistanceSelectedStart = mutableStateOf<Double?>(null)
    private val _filterDistanceSelectedEnd = mutableStateOf<Double?>(null)
    private val _filterDistanceTotalStart = mutableStateOf<Double?>(null)
    private val _filterDistanceTotalEnd = mutableStateOf<Double?>(null)
    private val _filterDistancePendingChangeStart = mutableStateOf<String?>(null) // not save
    private val _filterDistancePendingChangeEnd = mutableStateOf<String?>(null) // not save
    private val _filterTypes = mutableStateListOf<Pair<SportType, Boolean>>()

    // SIZE
    private val _sizeResolutionList =
        mutableStateListOf(*ResolutionListFactoryImpl().create().toTypedArray())
    private val _sizeResolutionListSelectedIndex = mutableStateOf(0)

    // SORT
    private val _sortTypeSelected = mutableStateOf(EditArtSortType.DATE)
    private val _sortDirectionTypeSelected = mutableStateOf(EditArtSortDirectionType.ASCENDING)

    // STYLE
    private val _styleActivities = mutableStateOf(ColorWrapper.White)
    private val _styleBackgroundList = (0 until 7).map {
        if (it % 2 == 0) ColorWrapper.Black else ColorWrapper.White
    }.toMutableStateList()

    private val _styleBackgroundAngleType = mutableStateOf(AngleType.CW90)
    private val _styleBackgroundGradientColorCount = mutableStateOf(2)
    private val _styleBackgroundType = mutableStateOf(BackgroundType.SOLID)
    private val _styleFont = mutableStateOf<ColorWrapper?>(null)
    private val _styleStrokeWidthType = mutableStateOf(StrokeWidthType.MEDIUM)

    // TYPE
    private val _typeActivitiesDistanceMetersSummed = mutableStateOf(0)
    private val _typeFontSelected = mutableStateOf(FontType.JOSEFIN_SANS)
    private val _typeFontWeightSelected = mutableStateOf(FontWeightType.REGULAR)
    private val _typeFontItalicized = mutableStateOf(false)
    private val _typeFontSizeSelected = mutableStateOf(FontSizeType.MEDIUM)
    private val _typeMaximumCustomTextLength = 30 // not save
    private val _typeLeftSelected = mutableStateOf(EditArtTypeType.NONE)
    private val _typeLeftCustomText = mutableStateOf("")
    private val _typeCenterSelected = mutableStateOf(EditArtTypeType.NONE)
    private val _typeCenterCustomText = mutableStateOf("")
    private val _typeRightSelected = mutableStateOf(EditArtTypeType.NONE)
    private val _typeRightCustomText = mutableStateOf("")

    /** endregion **/

    init {
        val loadingState = Loading(
            dialogActive = _dialogActive,
            pagerStateWrapper = PagerStateWrapper(
                pagerHeaders = EditArtHeaderType.values().toList(),
                pagerState = PagerState(EditArtHeaderType.values().toList().size),
                fadeLengthMs = 1000
            )
        )
        loadingState.push()

        viewModelScope.launch(Dispatchers.Default) {
            activities = getActivitiesFromDisk(athleteId)

            /** Restore any previous state **/
            stateRestore()
            Standby(
                dialogActive = _dialogActive,
                pagerStateWrapper = loadingState.pagerStateWrapper,
                filterActivitiesCountDate = _filterActivitiesCountDate,
                filterActivitiesCountDistance = _filterActivitiesCountDistance,
                filterActivitiesCountType = _filterActivitiesCountType,
                filterDateSelections = _filterDateSelections,
                filterDateSelectionIndex = _filterDateSelectionIndex,
                filterDistanceSelectedStart = _filterDistanceSelectedStart,
                filterDistanceSelectedEnd = _filterDistanceSelectedEnd,
                filterDistanceTotalStart = _filterDistanceTotalStart,
                filterDistanceTotalEnd = _filterDistanceTotalEnd,
                filterDistancePendingChangeStart = _filterDistancePendingChangeStart,
                filterDistancePendingChangeEnd = _filterDistancePendingChangeEnd,
                filterTypes = _filterTypes,
                previewBitmap = _bitmap,
                previewBitmapZoomed = _bitmapZoomed,
                sizeResolutionList = _sizeResolutionList,
                sizeResolutionListSelectedIndex = _sizeResolutionListSelectedIndex,
                sortDirectionTypeSelected = _sortDirectionTypeSelected,
                sortTypeSelected = _sortTypeSelected,
                styleActivities = _styleActivities,
                styleBackgroundAngleType = _styleBackgroundAngleType,
                styleBackgroundGradientColorCount = _styleBackgroundGradientColorCount,
                styleBackgroundList = _styleBackgroundList,
                styleBackgroundType = _styleBackgroundType,
                styleFont = _styleFont,
                styleStrokeWidthType = _styleStrokeWidthType,
                typeActivitiesDistanceMetersSummed = _typeActivitiesDistanceMetersSummed,
                typeFontSelected = _typeFontSelected,
                typeFontWeightSelected = _typeFontWeightSelected,
                typeFontItalicized = _typeFontItalicized,
                typeFontSizeSelected = _typeFontSizeSelected,
                typeMaximumCustomTextLength = _typeMaximumCustomTextLength,
                typeLeftSelected = _typeLeftSelected,
                typeLeftCustomText = _typeLeftCustomText,
                typeCenterSelected = _typeCenterSelected,
                typeCenterCustomText = _typeCenterCustomText,
                typeRightSelected = _typeRightSelected,
                typeRightCustomText = _typeRightCustomText
            ).push()

            EditArtFilterType.values().forEach {
                val hasRestoredSaveState = when (it) {
                    DATE -> _filterDateSelections.isNotEmpty()
                    TYPE -> _filterTypes.isNotEmpty()
                    DISTANCE -> _filterDistanceTotalStart.value != null
                }

                if (hasRestoredSaveState) {
                    /** If there was a saved state, now that we've pushed Standby update filtered activities to reflect
                     * the various saved filters. **/
                    it.updateFilteredActivities()
                } else {
                    activitiesFilteredByFilterType[it] = activities
                    it.pushUpdatedFilteredActivityCountToView()
                    it.updateFilters()
                }
            }

            /** Finally, update the previewBitmap of the current Standby state **/
            updateBitmap()

            /** Observe any changes to all states and save them into ssh **/
            stateObserveAndSave()
        }
    }

    override fun onEvent(event: EditArtViewEvent) {
        when (event) {
            is ArtMutatingEvent -> onArtMutatingEvent(event)
            is ClickedRemoveGradientColor -> onClickedRemoveGradientColor(event)
            is ClickedInfoCheckeredBackground -> onClickedInfoCheckeredBackground()
            is ClickedInfoGradientBackground -> onClickedInfoGradientBackground()
            is ClickedInfoTransparentBackground -> onClickedInfoTransparentBackground()
            is DialogDismissed -> onDialogDismissed()
            is DialogNavigateUpConfirmed -> onDialogNavigateUpConfirmed()
            is FilterDistancePendingChange -> onFilterDistancePendingChange(event)
            is NavigateUpClicked -> onNavigateUpClicked()
            is SaveClicked -> onSaveClicked()
            is SizeCustomPendingChanged -> onSizeCustomPendingChanged(event)
            is StyleColorPendingChanged -> onStyleColorPendingChanged(event)
            is PageHeaderClicked -> onPageHeaderClicked(event)
            is PreviewGestureClearZoom -> onPreviewGestureClearZoom()
            is PreviewGestureRenderZoom -> onPreviewGestureRenderZoom(event)
        }
    }

    private fun onArtMutatingEvent(event: ArtMutatingEvent) {
        when (event) {
            is FilterChanged -> onFilterChangeEvent(event)
            is PreviewSpaceMeasured -> onScreenMeasured(event)
            is SizeChanged -> onSizeChanged(event)
            is SizeCustomChanged -> onSizeCustomChanged(event)
            is SizeCustomPendingChangeConfirmed -> onSizeCustomPendingChangeConfirmed(event)
            is SizeRotated -> onSizeRotated(event)
            is SortDirectionChanged -> onSortDirectionChanged(event)
            is SortTypeChanged -> onSortTypeChanged(event)
            is StyleBackgroundColorAdded -> onStyleBackgroundColorAdded()
            is StyleBackgroundColorRemoveConfirmed -> onStyleBackgroundColorRemoveConfirmed()
            is StyleBackgroundTypeChanged -> onStyleBackgroundTypeChanged(event)
            is StyleColorChanged -> onStyleColorChanged(event)
            is StyleColorPendingChangeConfirmed -> onStyleColorPendingChangeConfirmed(event)
            is StyleColorFontUseCustomChanged -> onStyleColorFontUseCustomChanged(event)
            is StyleGradientAngleTypeChanged -> onStyleGradientAngleTypeChanged(event)
            is StyleStrokeWidthChanged -> onStyleStrokeWidthChanged(event)
            is TypeCustomTextChanged -> onTypeCustomTextChanged(event)
            is TypeFontChanged -> onTypeFontChanged(event)
            is TypeFontSizeChanged -> onTypeFontSizeChanged(event)
            is TypeFontWeightChanged -> onTypeFontWeightChanged(event)
            is TypeFontItalicChanged -> onTypeFontItalicChanged(event)
            is TypeSelectionChanged -> onTypeSelectionChanged(event)
        }
        if (event !is FilterChanged) {
            updateBitmap()
        }
    }

    private fun onFilterChangeEvent(event: FilterChanged) {
        /** Run all operations which require many linear scans of activities off of the main thread **/
        viewModelScope.launch(activitiesProcessingDispatcher) {
            /** Updates UI in response to adjustment without adjusted filtered activities **/
            when (event) {
                is FilterChanged.FilterDateSelectionChanged -> onFilterDateSelectionChanged(event)
                is FilterChanged.FilterDateCustomChanged -> onFilterDateCustomChanged(event)
                is FilterChanged.FilterDistanceChanged -> onFilterDistanceChanged(event)
                is FilterChanged.FilterDistancePendingChangeConfirmed -> onFilterDistancePendingChangeConfirmed(
                    event
                )
                is FilterChanged.FilterTypeToggled -> onFilterTypeToggled(event)
            }

            event.filterType.apply {
                /** In response to [EditArtViewState.Standby] changes relevant to this
                 * [EditArtFilterType] which just occurred, update its filtered activities **/
                updateFilteredActivities()
                /** For all subsequent [EditArtFilterType], update [EditArtViewState.Standby]
                 * in response to the activities they are filtering potentially having changed.
                 * This may change both the total number of [activitiesFilteredByFilterType]
                 * and the filters which the user interacts with. **/
                forEachNextFilterType {
                    it.updateFilteredActivities()
                    it.updateFilters()
                }
            }
            updateBitmap()
        }
    }

    private fun onClickedRemoveGradientColor(event: ClickedRemoveGradientColor) {
        _dialogActive.value = EditArtDialog.ConfirmDeleteGradientColor(
            toDeleteIndex = event.removedIndex
        )
    }

    private fun onClickedInfoCheckeredBackground() {
        _dialogActive.value = EditArtDialog.InfoCheckeredBackground
    }

    private fun onClickedInfoGradientBackground() {
        _dialogActive.value = EditArtDialog.InfoGradientBackground
    }

    private fun onClickedInfoTransparentBackground() {
        _dialogActive.value = EditArtDialog.InfoTransparent
    }

    private fun onDialogDismissed() {
        _dialogActive.value = EditArtDialog.None
    }

    private fun onDialogNavigateUpConfirmed() {
        _dialogActive.value = EditArtDialog.None
        viewModelScope.launch { routeTo(NavigateUp) }
    }

    private fun onFilterDateSelectionChanged(event: FilterChanged.FilterDateSelectionChanged) {
        _filterDateSelectionIndex.value = event.index
    }

    private fun onFilterDateCustomChanged(event: FilterChanged.FilterDateCustomChanged) {
        val customIndex = _filterDateSelections.indexOfFirst { it is DateSelection.Custom }
        val updatedDateSelection =
            (_filterDateSelections[customIndex] as DateSelection.Custom).run {
                copy(
                    dateSelectedStartUnixMs = when (event) {
                        is FilterChanged.FilterDateCustomChanged.FilterAfterChanged -> {
                            event.changedTo.coerceIn(dateTotal.first..dateSelected.last)
                        }
                        is FilterChanged.FilterDateCustomChanged.FilterBeforeChanged -> {
                            dateSelected.first
                        }
                    },
                    dateSelectedEndUnixMs = when (event) {
                        is FilterChanged.FilterDateCustomChanged.FilterAfterChanged -> {
                            dateSelected.last
                        }
                        is FilterChanged.FilterDateCustomChanged.FilterBeforeChanged -> {
                            event.changedTo.coerceIn(dateSelected.first..dateTotal.last)
                        }
                    }
                )
            }
        _filterDateSelectionIndex.value = customIndex
        _filterDateSelections[customIndex] = updatedDateSelection
    }

    private fun onFilterDistanceChanged(event: FilterChanged.FilterDistanceChanged) {
        _filterDistanceSelectedStart.value = event.changedTo.start
        _filterDistanceSelectedEnd.value = event.changedTo.endInclusive
        _filterDistancePendingChangeStart.value = null
        _filterDistancePendingChangeEnd.value = null
    }

    private fun onFilterDistancePendingChange(event: FilterDistancePendingChange) {
        if (event is FilterDistancePendingChange.FilterDistancePendingChangeShortest) {
            _filterDistancePendingChangeStart.value = event.changedTo
        } else {
            _filterDistancePendingChangeEnd.value = event.changedTo
        }
    }

    private fun onFilterDistancePendingChangeConfirmed(event: FilterChanged.FilterDistancePendingChangeConfirmed) {
        val totalValueSmallest = _filterDistanceTotalStart.value ?: 0.0
        val totalValueLargest = _filterDistanceTotalEnd.value ?: Double.MAX_VALUE

        when (event) {
            is FilterChanged.FilterDistancePendingChangeConfirmed.StartConfirmed -> {
                _filterDistancePendingChangeStart.value?.let { pendingStart ->
                    val coerceAtMost = _filterDistanceSelectedEnd.value ?: totalValueLargest
                    _filterDistanceSelectedStart.value = parseNumberFromStringUtils
                        .parse(pendingStart)
                        .milesToMeters()
                        .coerceIn(totalValueSmallest.rangeTo(coerceAtMost))
                    _filterDistanceSelectedEnd.value = coerceAtMost
                    _filterDistancePendingChangeStart.value = null
                }
            }
            is FilterChanged.FilterDistancePendingChangeConfirmed.EndConfirmed -> {
                _filterDistancePendingChangeEnd.value?.let { pendingEnd ->
                    val coerceAtLeast = _filterDistanceSelectedStart.value ?: totalValueSmallest
                    _filterDistanceSelectedStart.value = parseNumberFromStringUtils
                        .parse(pendingEnd)
                        .milesToMeters()
                        .coerceIn(coerceAtLeast.rangeTo(totalValueLargest))
                    _filterDistanceSelectedEnd.value = coerceAtLeast
                    _filterDistancePendingChangeEnd.value = null
                }
            }
        }
    }

    private fun onFilterTypeToggled(event: FilterChanged.FilterTypeToggled) {
        /** If an athlete clicks on a previous filter to TYPE which will result in [_filterTypes]
         * becoming smaller, then rapidly clicks on the last index before the list resizes,
         * there would be an [IndexOutOfBoundsException] if this logic was not used. **/
        _filterTypes
            .indexOfFirst { it.first == event.type }
            .takeIf { it != -1 }
            ?.let {
                _filterTypes[it] = _filterTypes[it].run {
                    copy(second = second.not())
                }
            }
    }

    private fun onNavigateUpClicked() {
        _dialogActive.value = EditArtDialog.NavigateUp
    }

    private fun onPageHeaderClicked(event: PageHeaderClicked) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            (lastPushedState as? Standby)?.run {
                pagerStateWrapper.pagerState.scrollToPage(event.position)
            }
        }
    }

    private fun onPreviewGestureClearZoom() {
        imageZoomedJob?.cancel()
        _bitmapZoomed.value = null
    }

    private fun onPreviewGestureRenderZoom(event: PreviewGestureRenderZoom) {
        _bitmapZoomed.value = null
        imageZoomedJob?.cancel()
        imageZoomedJob = viewModelScope.launch(imageZoomedProcessingDispatcher) {
            delay(1000L)
            _bitmapZoomed.value = Bitmap.createBitmap(
                (previewScreenSize?.width ?: 0f).toInt(),
                (previewScreenSize?.height ?: 0f).toInt(),
                Bitmap.Config.ARGB_8888
            ).also { bitmap ->
                Canvas(bitmap).apply {
                    drawRect(
                        0f,
                        0f,
                        width.toFloat(),
                        height.toFloat(),
                        Paint().apply { color = Color.DKGRAY }
                    )
                }
            }
        }
    }

    private fun onSaveClicked() {
        viewModelScope.launch(activitiesProcessingDispatcher) {
            /** Prevent rapid-click after changing a filter from routing to SaveArt when
             * no activities are selected.
             *
             * Clicking SaveArt rapidly and then changing a filter will not cause issue on
             * SaveArt, but on back-press that filter will have taken effect. */
            if (activitiesFiltered.isEmpty()) {
                return@launch
            }

            val targetSize = _sizeResolutionList[_sizeResolutionListSelectedIndex.value]
            val filterRange = activitiesDateRangeUnixMs

            routeTo(
                MainDestination.NavigateSaveArt(
                    activityTypes = filteredTypes,
                    athleteId = athleteId,
                    backgroundColorsArgb = _styleBackgroundList
                        .take(_styleBackgroundGradientColorCount.value)
                        .map { it.toColorArgb() }, // TODO
                    backgroundAngleType = _styleBackgroundAngleType.value,
                    backgroundType = _styleBackgroundType.value,
                    colorActivitiesArgb = _styleActivities.value.toColorArgb(),
                    colorFontArgb = (_styleFont.value ?: _styleActivities.value).toColorArgb(),
                    filterAfterMs = filterRange?.first ?: Long.MIN_VALUE,
                    filterBeforeMs = filterRange?.last ?: Long.MAX_VALUE,
                    filterDistanceLessThanMeters = _filterDistanceSelectedEnd.value
                        ?.roundToInt()
                        ?: Int.MAX_VALUE,
                    filterDistanceMoreThanMeters = _filterDistanceSelectedStart.value
                        ?.roundToInt()
                        ?: Int.MIN_VALUE,
                    sizeHeight = targetSize.sizeHeightPx,
                    sizeWidth = targetSize.sizeWidthPx,
                    sortType = _sortTypeSelected.value,
                    sortDirectionType = _sortDirectionTypeSelected.value,
                    strokeWidthType = _styleStrokeWidthType.value,
                    textLeft = EditArtTypeSection.LEFT.text,
                    textCenter = EditArtTypeSection.CENTER.text,
                    textRight = EditArtTypeSection.RIGHT.text,
                    textFontAssetPath = _typeFontSelected.value.getAssetPath(
                        fontWeightType = _typeFontWeightSelected.value,
                        italicized = _typeFontItalicized.value
                    ),
                    textFontSize = _typeFontSizeSelected.value
                )
            )
        }
    }

    private fun onStyleColorPendingChanged(event: StyleColorPendingChanged) {
        when (event.style) {
            is StyleIdentifier.Activities -> {
                val color = _styleActivities.value
                _styleActivities.value = color.copyPendingChange(
                    colorType = event.colorType,
                    changedTo = event.changedTo
                )
            }
            is StyleIdentifier.Background -> {
                val color = _styleBackgroundList[event.style.index]
                _styleBackgroundList[event.style.index] = color.copyPendingChange(
                    colorType = event.colorType,
                    changedTo = event.changedTo
                )
            }
            is StyleIdentifier.Font -> {
                val color = _styleFont.value ?: _styleActivities.value
                _styleFont.value = color.copyPendingChange(
                    colorType = event.colorType,
                    changedTo = event.changedTo
                )
            }
        }
    }

    private fun onScreenMeasured(event: PreviewSpaceMeasured) {
        previewScreenSize = event.size
    }

    private fun onSizeChanged(event: SizeChanged) {
        _sizeResolutionListSelectedIndex.value = event.changedIndex
    }

    private fun onSizeCustomChanged(event: SizeCustomChanged) {
        val newRes = (_sizeResolutionList[event.index] as Resolution.CustomResolution).run {
            copy(
                pendingHeight = if (event.heightChanged) null else pendingHeight,
                pendingWidth = if (event.heightChanged) pendingWidth else null,
                sizeHeightPx = if (event.heightChanged) event.changedTo else sizeHeightPx,
                sizeWidthPx = if (event.heightChanged) sizeWidthPx else event.changedTo
            )
        }
        _sizeResolutionList[event.index] = newRes
        _sizeResolutionListSelectedIndex.value = event.index
    }

    private fun onSizeCustomPendingChangeConfirmed(event: SizeCustomPendingChangeConfirmed) {
        val customRes = _sizeResolutionList[event.customIndex] as Resolution.CustomResolution
        val range = customRes.sizeRangePx
        _sizeResolutionList[event.customIndex] = customRes.run {
            copy(
                pendingHeight = null,
                pendingWidth = null,
                sizeHeightPx = pendingHeight?.let {
                    parseNumberFromStringUtils.parse(it).toFloat().coerceIn(range)
                } ?: sizeHeightPx,
                sizeWidthPx = pendingWidth?.let {
                    parseNumberFromStringUtils.parse(it).toFloat().coerceIn(range)
                } ?: sizeWidthPx
            )
        }
        _sizeResolutionListSelectedIndex.value = event.customIndex
    }

    private fun onSizeCustomPendingChanged(event: SizeCustomPendingChanged) {
        _sizeResolutionList.apply {
            val tarIndex = indexOfFirst { it is Resolution.CustomResolution }
            set(tarIndex, (get(tarIndex) as Resolution.CustomResolution).run {
                copy(
                    pendingWidth = if (event is SizeCustomPendingChanged.WidthChanged) event.changedTo else pendingWidth,
                    pendingHeight = if (event is SizeCustomPendingChanged.HeightChanged) event.changedTo else pendingHeight
                )
            })
        }
    }

    private fun onSizeRotated(event: SizeRotated) {
        val prevRes = _sizeResolutionList[event.rotatedIndex]
        val adjRes = (prevRes as? Resolution.RotatingResolution
            ?: error("Rotated a non-rotating resolution."))
            .copyWithRotation()
        _sizeResolutionList[event.rotatedIndex] = adjRes
    }

    private fun onSortDirectionChanged(event: SortDirectionChanged) {
        _sortDirectionTypeSelected.value = event.changedTo
    }

    private fun onSortTypeChanged(event: SortTypeChanged) {
        _sortTypeSelected.value = event.changedTo
    }

    private fun onStyleBackgroundColorAdded() {
        val prevCount = _styleBackgroundGradientColorCount.value
        if (prevCount >= 7) return // todo
        _styleBackgroundGradientColorCount.value = prevCount.inc()
    }

    private fun onStyleBackgroundColorRemoveConfirmed() {
        val prevCount = _styleBackgroundGradientColorCount.value
        if (prevCount <= 2) return // todo
        val removedIndex = (_dialogActive.value as? EditArtDialog.ConfirmDeleteGradientColor)
            ?.toDeleteIndex
            ?: return
        for (i in removedIndex until _styleBackgroundList.lastIndex) {
            val newColor = _styleBackgroundList[i + 1]
            _styleBackgroundList[i] = _styleBackgroundList[i].copy(
                red = newColor.red,
                green = newColor.green,
                blue = newColor.blue
            )
        }
        _dialogActive.value = EditArtDialog.None
        _styleBackgroundGradientColorCount.value = prevCount.dec()
    }

    private fun onStyleBackgroundTypeChanged(event: StyleBackgroundTypeChanged) {
        _styleBackgroundType.value = event.changedTo
    }

    private fun onStyleColorChanged(event: StyleColorChanged) {
        when (event.style) {
            is StyleIdentifier.Activities -> _styleActivities.value =
                _styleActivities.value.copyWithChange(
                    colorType = event.colorType,
                    changedTo = event.changedTo
                )
            is StyleIdentifier.Background -> {
                val color = _styleBackgroundList[event.style.index]
                _styleBackgroundList[event.style.index] = color.copyWithChange(
                    colorType = event.colorType,
                    changedTo = event.changedTo
                )
            }
            is StyleIdentifier.Font -> _styleFont.value =
                (_styleFont.value ?: _styleActivities.value).copyWithChange(
                    colorType = event.colorType,
                    changedTo = event.changedTo
                )
        }
    }

    private fun onStyleColorPendingChangeConfirmed(event: StyleColorPendingChangeConfirmed) {
        when (event.style) {
            is StyleIdentifier.Activities -> _styleActivities.value =
                _styleActivities.value.confirmPendingChanges()
            is StyleIdentifier.Background -> {
                val color = _styleBackgroundList[event.style.index]
                _styleBackgroundList[event.style.index] = color.confirmPendingChanges()
            }
            is StyleIdentifier.Font -> _styleFont.value = _styleFont.value?.confirmPendingChanges()
        }
    }

    private fun onStyleColorFontUseCustomChanged(event: StyleColorFontUseCustomChanged) {
        _styleFont.value = if (event.useCustom) {
            _styleFont.value ?: _styleActivities.value
        } else {
            null
        }
    }

    private fun onStyleGradientAngleTypeChanged(event: StyleGradientAngleTypeChanged) {
        _styleBackgroundAngleType.value = event.changedTo
    }

    private fun onStyleStrokeWidthChanged(event: StyleStrokeWidthChanged) {
        _styleStrokeWidthType.value = event.changedTo
    }

    private fun onTypeCustomTextChanged(event: TypeCustomTextChanged) {
        val newText = event.changedTo.takeIf { it.length <= _typeMaximumCustomTextLength }
        when (event.section) {
            EditArtTypeSection.LEFT -> {
                _typeLeftCustomText.value = newText ?: _typeLeftCustomText.value
                _typeLeftSelected.value = EditArtTypeType.CUSTOM
            }
            EditArtTypeSection.CENTER -> {
                _typeCenterCustomText.value = newText ?: _typeCenterCustomText.value
                _typeCenterSelected.value = EditArtTypeType.CUSTOM

            }
            EditArtTypeSection.RIGHT -> {
                _typeRightCustomText.value = newText ?: _typeRightCustomText.value
                _typeRightSelected.value = EditArtTypeType.CUSTOM
            }
        }
    }

    private fun onTypeFontChanged(event: TypeFontChanged) {
        event.changedTo.run {
            _typeFontItalicized.value = _typeFontItalicized.value && isItalic
            _typeFontSelected.value = this
            _typeFontWeightSelected.value =
                if (fontWeightTypes.contains(_typeFontWeightSelected.value)) {
                    _typeFontWeightSelected.value
                } else {
                    FontWeightType.REGULAR
                }
        }
    }

    private fun onTypeFontSizeChanged(event: TypeFontSizeChanged) {
        _typeFontSizeSelected.value = event.changedTo
    }

    private fun onTypeSelectionChanged(event: TypeSelectionChanged) {
        when (event.section) {
            EditArtTypeSection.LEFT -> _typeLeftSelected.value = event.typeSelected
            EditArtTypeSection.CENTER -> _typeCenterSelected.value = event.typeSelected
            EditArtTypeSection.RIGHT -> _typeRightSelected.value = event.typeSelected
        }
    }

    private fun onTypeFontWeightChanged(event: TypeFontWeightChanged) {
        _typeFontWeightSelected.value = event.changedTo
    }

    private fun onTypeFontItalicChanged(event: TypeFontItalicChanged) {
        _typeFontItalicized.value = event.changedTo
    }

    private fun ColorWrapper.copyWithChange(
        colorType: ColorType,
        changedTo: Float
    ): ColorWrapper {
        return when (colorType) {
            ALPHA -> copy(alpha = changedTo, pendingAlpha = null)
            BLUE -> copy(blue = changedTo, pendingBlue = null)
            GREEN -> copy(green = changedTo, pendingGreen = null)
            RED -> copy(red = changedTo, pendingRed = null)
        }
    }

    private fun ColorWrapper.copyPendingChange(
        colorType: ColorType,
        changedTo: String
    ): ColorWrapper {
        return when (colorType) {
            ALPHA -> copy(pendingAlpha = changedTo)
            BLUE -> copy(pendingBlue = changedTo)
            GREEN -> copy(pendingGreen = changedTo)
            RED -> copy(pendingRed = changedTo)
        }
    }

    private fun ColorWrapper.confirmPendingChanges(): ColorWrapper {
        val parseEightBitColorToRatio: (String) -> Float = {
            ColorWrapper.eightBitToRatio(
                parseNumberFromStringUtils
                    .parse(it)
                    .toInt()
                    .coerceIn(ColorWrapper.EIGHT_BIT_RANGE)
            )
        }
        return copy(
            alpha = pendingAlpha?.let { parseEightBitColorToRatio(it) } ?: alpha,
            blue = pendingBlue?.let { parseEightBitColorToRatio(it) } ?: blue,
            green = pendingGreen?.let { parseEightBitColorToRatio(it) } ?: green,
            red = pendingRed?.let { parseEightBitColorToRatio(it) } ?: red,
            pendingAlpha = null,
            pendingBlue = null,
            pendingGreen = null,
            pendingRed = null
        )
    }

    private fun updateBitmap() {
        _bitmap.value = null
        _bitmapZoomed.value = null
        imageJob?.cancel()
        imageJob = viewModelScope.launch(imageProcessingDispatcher) {
            previewScreenSize?.let {
                val newBitmap = visualizationUtils.createBitmap(
                    activities = activitiesFiltered,
                    backgroundAngleType = _styleBackgroundAngleType.value,
                    backgroundType = _styleBackgroundType.value,
                    backgroundColorsArgb = _styleBackgroundList
                        .take(_styleBackgroundGradientColorCount.value)
                        .map { it.toColorArgb() }, // TODO
                    colorActivitiesArgb = _styleActivities.value.toColorArgb(),
                    colorFontArgb = (_styleFont.value ?: _styleActivities.value).toColorArgb(),
                    strokeWidth = _styleStrokeWidthType.value,
                    bitmapSize = imageSizeUtils.sizeToMaximumSize(
                        actualSize = _sizeResolutionList[_sizeResolutionListSelectedIndex.value].run {
                            Size(width = sizeWidthPx, height = sizeHeightPx)
                        },
                        maximumSize = it
                    ),
                    fontAssetPath = _typeFontSelected.value.getAssetPath(
                        fontWeightType = _typeFontWeightSelected.value,
                        italicized = _typeFontItalicized.value
                    ),
                    fontSize = _typeFontSizeSelected.value,
                    isPreview = true,
                    sortType = _sortTypeSelected.value,
                    sortDirectionType = _sortDirectionTypeSelected.value,
                    textLeft = EditArtTypeSection.LEFT.text,
                    textCenter = EditArtTypeSection.CENTER.text,
                    textRight = EditArtTypeSection.RIGHT.text
                )
                _bitmap.value = newBitmap
            }

        }
    }

    private val EditArtTypeSection.text: String?
        get() {
            val typeCustomText: Pair<EditArtTypeType, String> = when (this@text) {
                EditArtTypeSection.CENTER -> _typeCenterSelected.value to _typeCenterCustomText.value
                EditArtTypeSection.LEFT -> _typeLeftSelected.value to _typeLeftCustomText.value
                EditArtTypeSection.RIGHT -> _typeRightSelected.value to _typeRightCustomText.value
            }

            return when (typeCustomText.first) {
                EditArtTypeType.NONE -> null
                EditArtTypeType.DISTANCE_MILES -> activitiesSummedDistance.meterToMilesStr()
                EditArtTypeType.DISTANCE_KILOMETERS -> activitiesSummedDistance.meterToKilometerStr()
                EditArtTypeType.CUSTOM -> typeCustomText.second.takeIf { it.isNotBlank() }
            }
        }


    private fun Double.meterToMilesStr(): String = "${(this * 0.000621371192).roundToInt()} mi"
    private fun Double.meterToKilometerStr(): String = "${(this / 1000f).roundToInt()} km"
    private fun Double.milesToMeters(): Double = this / 0.000621371192

    private val filteredTypes: List<SportType>
        get() = _filterTypes.filter { it.second }.map { it.first }

    private val filteredDistanceRangeMeters: IntRange
        get() = (_filterDistanceSelectedStart.value
            ?.roundToInt()
            ?: Int.MIN_VALUE)
            .rangeTo(
                _filterDistanceSelectedEnd.value
                    ?.roundToInt()
                    ?: Int.MAX_VALUE
            )

    private fun stateRestore() {
        ssh.get<Int>(SSH_FILTER_ACTIVITIES_COUNT_DATE)?.let {
            _filterActivitiesCountDate.value = it
        }
        ssh.get<Int>(SSH_FILTER_ACTIVITIES_COUNT_DISTANCE)?.let {
            _filterActivitiesCountDistance.value = it
        }
        ssh.get<Int>(SSH_FILTER_ACTIVITIES_COUNT_TYPE)?.let {
            _filterActivitiesCountType.value = it
        }
        ssh.get<List<DateSelection>>(SSH_FILTER_DATE_SELECTIONS)?.let {
            _filterDateSelections.clear()
            _filterDateSelections.addAll(it)
        }
        ssh.get<Int>(SSH_FILTER_DATE_SELECTION_INDEX)?.let {
            _filterDateSelectionIndex.value = it
        }
        ssh.get<Double?>(SSH_FILTER_DISTANCE_SELECTED_START)?.let {
            _filterDistanceSelectedStart.value = it
        }
        ssh.get<Double?>(SSH_FILTER_DISTANCE_SELECTED_END)?.let {
            _filterDistanceSelectedEnd.value = it
        }
        ssh.get<Double?>(SSH_FILTER_DISTANCE_TOTAL_START)?.let {
            _filterDistanceTotalStart.value = it
        }
        ssh.get<Double?>(SSH_FILTER_DISTANCE_TOTAL_END)?.let {
            _filterDistanceTotalEnd.value = it
        }
        ssh.get<List<Pair<SportType, Boolean>>>(SSH_FILTER_TYPES)?.let {
            _filterTypes.clear()
            _filterTypes.addAll(it)
        }
        ssh.get<List<Resolution>>(SSH_SIZE_RESOLUTION_LIST)?.let {
            _sizeResolutionList.clear()
            _sizeResolutionList.addAll(it)
        }
        ssh.get<Int>(SSH_SIZE_RESOLUTION_LIST_SELECTED_INDEX)?.let {
            _sizeResolutionListSelectedIndex.value = it
        }
        ssh.get<EditArtSortType>(SSH_SORT_TYPE_SELECTED)?.let {
            _sortTypeSelected.value = it
        }
        ssh.get<EditArtSortDirectionType>(SSH_SORT_DIRECTION_TYPE_SELECTED)?.let {
            _sortDirectionTypeSelected.value = it
        }
        ssh.get<ColorWrapper>(SSH_STYLE_ACTIVITIES)?.let {
            _styleActivities.value = it
        }
        ssh.get<List<ColorWrapper>>(SSH_STYLE_BACKGROUND_LIST)?.let {
            _styleBackgroundList.clear()
            _styleBackgroundList.addAll(it)
        }
        ssh.get<AngleType>(SSH_STYLE_BACKGROUND_ANGLE_TYPE)?.let {
            _styleBackgroundAngleType.value = it
        }
        ssh.get<Int>(SSH_STYLE_BACKGROUND_GRADIENT_COLOR_COUNT)?.let {
            _styleBackgroundGradientColorCount.value = it
        }
        ssh.get<BackgroundType>(SSH_STYLE_BACKGROUND_TYPE)?.let {
            _styleBackgroundType.value = it
        }
        ssh.get<ColorWrapper>(SSH_STYLE_FONT)?.let {
            _styleFont.value = it
        }
        ssh.get<StrokeWidthType>(SSH_STYLE_STROKE_WIDTH_TYPE)?.let {
            _styleStrokeWidthType.value = it
        }
        ssh.get<Int>(SSH_TYPE_ACTIVITIES_DISTANCE_METERS_SUMMED)?.let {
            _typeActivitiesDistanceMetersSummed.value = it
        }
        ssh.get<FontType>(SSH_TYPE_FONT_SELECTED)?.let {
            _typeFontSelected.value = it
        }
        ssh.get<FontType>(SSH_TYPE_FONT_SELECTED)?.let {
            _typeFontSelected.value = it
        }
        ssh.get<FontWeightType>(SSH_TYPE_FONT_WEIGHT_SELECTED)?.let {
            _typeFontWeightSelected.value = it
        }
        ssh.get<Boolean>(SSH_TYPE_FONT_ITALICIZED)?.let {
            _typeFontItalicized.value = it
        }
        ssh.get<FontSizeType>(SSH_TYPE_FONT_SIZE_SELECTED)?.let {
            _typeFontSizeSelected.value = it
        }
        ssh.get<EditArtTypeType>(SSH_TYPE_LEFT_SELECTED)?.let {
            _typeLeftSelected.value = it
        }
        ssh.get<String>(SSH_TYPE_LEFT_CUSTOM_TEXT)?.let {
            _typeLeftCustomText.value = it
        }
        ssh.get<EditArtTypeType>(SSH_TYPE_CENTER_SELECTED)?.let {
            _typeCenterSelected.value = it
        }
        ssh.get<String>(SSH_TYPE_CENTER_CUSTOM_TEXT)?.let {
            _typeCenterCustomText.value = it
        }
        ssh.get<EditArtTypeType>(SSH_TYPE_RIGHT_SELECTED)?.let {
            _typeRightSelected.value = it
        }
        ssh.get<String>(SSH_TYPE_RIGHT_CUSTOM_TEXT)?.let {
            _typeRightCustomText.value = it
        }
    }

    private suspend fun stateObserveAndSave() {
        viewModelScope.launch {
            snapshotFlow { _styleBackgroundList.toList() }.collect {
                println("In the snapshot flow here for $it")
                ssh[SSH_STYLE_BACKGROUND_LIST] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _filterDateSelections.toList() }.collect {
                ssh[SSH_FILTER_DATE_SELECTIONS] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _filterTypes.toList() }.collect {
                ssh[SSH_FILTER_TYPES] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _sizeResolutionList.toList() }.collect {
                ssh[SSH_SIZE_RESOLUTION_LIST] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _filterActivitiesCountDate.value }.collect {
                ssh[SSH_FILTER_ACTIVITIES_COUNT_DATE] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _filterActivitiesCountDistance.value }.collect {
                ssh[SSH_FILTER_ACTIVITIES_COUNT_DISTANCE] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _filterActivitiesCountType.value }.collect {
                ssh[SSH_FILTER_ACTIVITIES_COUNT_TYPE] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _filterDateSelectionIndex.value }.collect {
                ssh[SSH_FILTER_DATE_SELECTION_INDEX] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _filterDistanceSelectedStart.value }.collect {
                ssh[SSH_FILTER_DISTANCE_SELECTED_START] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _filterDistanceSelectedEnd.value }.collect {
                ssh[SSH_FILTER_DISTANCE_SELECTED_END] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _filterDistanceTotalStart.value }.collect {
                ssh[SSH_FILTER_DISTANCE_TOTAL_START] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _filterDistanceTotalEnd.value }.collect {
                ssh[SSH_FILTER_DISTANCE_TOTAL_END] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _sizeResolutionListSelectedIndex.value }.collect {
                ssh[SSH_SIZE_RESOLUTION_LIST_SELECTED_INDEX] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _sortTypeSelected.value }.collect { ssh[SSH_SORT_TYPE_SELECTED] = it }
        }
        viewModelScope.launch {
            snapshotFlow { _sortDirectionTypeSelected.value }.collect {
                ssh[SSH_SORT_DIRECTION_TYPE_SELECTED] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _styleActivities.value }.collect {
                ssh[SSH_STYLE_ACTIVITIES] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _styleBackgroundAngleType.value }.collect {
                ssh[SSH_STYLE_BACKGROUND_ANGLE_TYPE] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _styleBackgroundGradientColorCount.value }.collect {
                ssh[SSH_STYLE_BACKGROUND_GRADIENT_COLOR_COUNT] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _styleBackgroundType.value }.collect {
                ssh[SSH_STYLE_BACKGROUND_TYPE] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _styleFont.value }.collect { ssh[SSH_STYLE_FONT] = it }
        }
        viewModelScope.launch {
            snapshotFlow { _styleStrokeWidthType.value }.collect {
                ssh[SSH_STYLE_STROKE_WIDTH_TYPE] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _typeActivitiesDistanceMetersSummed.value }.collect {
                ssh[SSH_TYPE_ACTIVITIES_DISTANCE_METERS_SUMMED] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _typeFontSelected.value }.collect { ssh[SSH_TYPE_FONT_SELECTED] = it }
        }
        viewModelScope.launch {
            snapshotFlow { _typeFontWeightSelected.value }.collect {
                ssh[SSH_TYPE_FONT_WEIGHT_SELECTED] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _typeFontItalicized.value }.collect {
                ssh[SSH_TYPE_FONT_ITALICIZED] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _typeFontSizeSelected.value }.collect {
                ssh[SSH_TYPE_FONT_SIZE_SELECTED] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _typeLeftSelected.value }.collect {
                ssh[SSH_TYPE_LEFT_SELECTED] = it
            }
        }
        viewModelScope.launch {
            snapshotFlow { _typeLeftCustomText.value }.collect {
                ssh[SSH_TYPE_LEFT_CUSTOM_TEXT] = it
            }
        }
    }
}