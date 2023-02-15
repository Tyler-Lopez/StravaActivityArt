package com.activityartapp.presentation.editArtScreen

import android.util.Size
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.useCase.activities.GetActivitiesFromMemory
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.MainDestination.NavigateSaveArt
import com.activityartapp.presentation.MainDestination.NavigateUp
import com.activityartapp.presentation.editArtScreen.ColorType.*
import com.activityartapp.presentation.editArtScreen.EditArtFilterType.*
import com.activityartapp.presentation.editArtScreen.EditArtFilterType.Companion.filterFinal
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.*
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.*
import com.activityartapp.presentation.editArtScreen.EditArtViewState.Loading
import com.activityartapp.presentation.editArtScreen.EditArtViewState.Standby
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeSection
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeSection.*
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeType
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeType.*
import com.activityartapp.util.*
import com.activityartapp.util.enums.FontWeightType
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagerApi::class)
@HiltViewModel
class EditArtViewModel @Inject constructor(
    getActivitiesFromMemory: GetActivitiesFromMemory,
    private val activityFilterUtils: ActivityFilterUtils,
    private val imageSizeUtils: ImageSizeUtils,
    private val timeUtils: TimeUtils,
    private val visualizationUtils: VisualizationUtils,
    private val savedStateHandle: SavedStateHandle,
    private val parseNumberFromStringUtils: ParseNumberFromStringUtils
) : BaseRoutingViewModel<EditArtViewState, EditArtViewEvent, MainDestination>() {

    companion object {
        private const val PREVIEW_BITMAP_MAX_SIZE_WIDTH_PX = 2000
        private const val PREVIEW_BITMAP_MAX_SIZE_HEIGHT_PX = 2000

        private const val STANDBY_SAVE_STATE_KEY = "StandbySaveState"

        private const val INDEX_FIRST = 0
    }

    /** All activities cached in Singleton memory **/
    private val activities: List<Activity> = getActivitiesFromMemory()

    /** The list of activities for each [EditArtFilterType] **/
    private val activitiesFilteredByFilterType: MutableMap<EditArtFilterType, List<Activity>> =
        mutableMapOf()

    /** The activities which are drawn after all filters have been applied **/
    private val activitiesFiltered: List<Activity>
        get() = activitiesFilteredByFilterType[filterFinal] ?: activities

    /** Determine current start and end MS of selected Date filters  **/
    private val activitiesDateRangeUnixMs: LongProgression?
        get() = state?.filterDateSelections?.let { selections ->
            val index = (lastPushedState as? Standby)?.filterDateSelectionIndex
                ?: error("Attempted to compute date range when not in Standby state.")

            selections[index].run {
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
        withLastState {
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
    }

    /** For a given [EditArtFilterType], update filters the user may choose which
     * are encapsulated within [Standby] **/
    private fun EditArtFilterType.updateFilters() {
        val filteredActivities = (activitiesFilteredByFilterType[lastFilter] ?: activities)
        withLastState {
            when (this@updateFilters) {
                DATE -> {
                    var selectionIndex = INDEX_FIRST
                    val selections = activityFilterUtils
                        .getPossibleDateSelections(
                            activities = filteredActivities,
                            customRangeSelectedPreviousMs = filterDateSelections
                                ?.find { it is DateSelection.Custom }
                                ?.run { this as? DateSelection.Custom }
                                ?.run { dateSelected }
                        )
                        ?.also {
                            selectionIndex = filterDateSelectionIndex
                                .takeIf { prevIndex -> prevIndex in INDEX_FIRST..it.lastIndex }
                                ?: INDEX_FIRST
                        }
                    copy(
                        filterDateSelections = selections,
                        filterDateSelectionIndex = selectionIndex
                    )
                }
                TYPE -> copy(
                    filterTypes = activityFilterUtils.getPossibleActivityTypes(
                        activities = filteredActivities,
                        filterTypesPrevious = filterTypes
                    )
                )
                DISTANCE -> {
                    val newRangeTotal = activityFilterUtils.getPossibleDistances(filteredActivities)
                    val newRangeSelected = newRangeTotal?.let { range ->
                        val adjStart = filterDistanceSelectedStart?.takeIf {
                            it >= range.start && it <= range.endInclusive
                        } ?: range.start
                        val adjEnd = filterDistanceSelectedEnd?.takeIf {
                            it >= range.start && it <= range.endInclusive
                        } ?: range.endInclusive
                        adjStart..adjEnd
                    }
                    copy(
                        filterDistancePendingChangeStart = null,
                        filterDistancePendingChangeEnd = null,
                        filterDistanceSelectedStart = newRangeSelected?.start,
                        filterDistanceSelectedEnd = newRangeSelected?.endInclusive,
                        filterDistanceTotalStart = newRangeTotal?.start,
                        filterDistanceTotalEnd = newRangeTotal?.endInclusive
                    )
                }
            }.push()
        }
    }

    private val EditArtFilterType.activitiesCount: Int
        get() = activitiesFilteredByFilterType[this]?.size ?: 0

    private fun EditArtFilterType.pushUpdatedFilteredActivityCountToView() {
        copyLastState {
            when (this@pushUpdatedFilteredActivityCountToView) {
                DATE -> copy(filterActivitiesCountDate = DATE.activitiesCount)
                DISTANCE -> copy(filterActivitiesCountDistance = DISTANCE.activitiesCount)
                TYPE -> copy(filterActivitiesCountType = TYPE.activitiesCount)
            }
        }.run {
            if (this@pushUpdatedFilteredActivityCountToView == filterFinal) {
                copy(typeActivitiesDistanceMetersSummed = activitiesSummedDistance.roundToInt())
            } else {
                this
            }
        }.push()
    }

    private val activitiesProcessingDispatcher by lazy { Dispatchers.Default.limitedParallelism(1) }
    private val imageProcessingDispatcher by lazy { Dispatchers.Default.limitedParallelism(1) }

    init {
        Loading().push()
        viewModelScope.launch {
            /** Push either a previously-saved (if available) or constructed Standby state **/
            val prevState: Standby? = savedStateHandle[STANDBY_SAVE_STATE_KEY]
            (prevState ?: Standby()).push()

            EditArtFilterType.values().forEach {
                if (prevState != null) {
                    /** If there was a saved state, now that we've pushed Standby update filtered activities to reflect
                     * the various saved filters. **/
                    it.updateFilteredActivities()
                } else {
                    /** Otherwise, simply initialize filtered activities for each sportType as all activities and
                     * initialize filters. **/
                    activitiesFilteredByFilterType[it] = activities
                    it.pushUpdatedFilteredActivityCountToView()
                    it.updateFilters()
                }
            }

            /** Finally, update the bitmap of the current Standby state **/
            updateBitmap()
        }
    }

    override fun onEvent(event: EditArtViewEvent) {
        when (event) {
            is ArtMutatingEvent -> onArtMutatingEvent(event)
            is ClickedInfoCheckeredBackground -> onClickedInfoCheckeredBackground()
            is ClickedInfoTransparentBackground -> onClickedInfoTransparentBackground()
            is DialogDismissed -> onDialogDismissed()
            is DialogNavigateUpConfirmed -> onDialogNavigateUpConfirmed()
            is FilterDistancePendingChange -> onFilterDistancePendingChange(event)
            is NavigateUpClicked -> onNavigateUpClicked()
            is SaveClicked -> onSaveClicked()
            is PageHeaderClicked -> onPageHeaderClicked(event)
        }
    }

    private fun onArtMutatingEvent(event: ArtMutatingEvent) {
        when (event) {
            is FilterChanged -> onFilterChangeEvent(event)
            is SizeChanged -> onSizeChanged(event)
            is SizeCustomChanged -> onSizeCustomChanged(event)
            is SizeRotated -> onSizeRotated(event)
            is SortDirectionChanged -> onSortDirectionChanged(event)
            is SortTypeChanged -> onSortTypeChanged(event)
            is StyleBackgroundTypeChanged -> onStyleBackgroundTypeChanged(event)
            is StyleColorActivitiesChanged -> onStyleColorActivitiesChanged(event)
            is StyleColorsBackgroundChanged -> onStyleColorsBackgroundChanged(event)
            is StyleColorFontChanged -> onStyleColorFontChanged(event)
            is StyleColorFontUseCustomChanged -> onStyleColorFontUseCustomChanged(event)
            is StylesStrokeWidthChanged -> onStylesStrokeWidthChanged(event)
            is TypeCustomTextChanged -> onTypeCustomTextChanged(event)
            is TypeFontChanged -> onTypeFontChanged(event)
            is TypeFontSizeChanged -> onTypeFontSizeChanged(event)
            is TypeFontWeightChanged -> onTypeFontWeightChanged(event)
            is TypeFontItalicChanged -> onTypeFontItalicChanged(event)
            is TypeSelectionChanged -> onTypeSelectionChanged(event)
        }
        if (event !is FilterChanged) {
            updateBitmap()
            savedStateHandle[STANDBY_SAVE_STATE_KEY] = (lastPushedState as? Standby)
        }
    }

    private fun onFilterChangeEvent(event: FilterChanged) {
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

        /** Run all operations which require many linear scans of activities off of the main thread **/
        viewModelScope.launch(activitiesProcessingDispatcher) {
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
            savedStateHandle[STANDBY_SAVE_STATE_KEY] = (lastPushedState as? Standby)
        }
    }

    private fun onClickedInfoCheckeredBackground() {
        pushStateCopy { copy(dialogActive = EditArtDialogType.INFO_CHECKERED_BACKGROUND) }
    }

    private fun onClickedInfoTransparentBackground() {
        pushStateCopy { copy(dialogActive = EditArtDialogType.INFO_TRANSPARENT) }
    }

    private fun onDialogDismissed() {
        pushStateCopy { copy(dialogActive = EditArtDialogType.NONE) }
    }

    private fun onDialogNavigateUpConfirmed() {
        copyLastState { copy(dialogActive = EditArtDialogType.NONE) }.push()
        viewModelScope.launch { routeTo(NavigateUp) }
    }

    private fun onFilterDateSelectionChanged(event: FilterChanged.FilterDateSelectionChanged) {
        copyLastState { copy(filterDateSelectionIndex = event.index) }.push()
    }

    private fun onFilterDateCustomChanged(event: FilterChanged.FilterDateCustomChanged) {
        withLastState {
            val replacementCustom: DateSelection.Custom =
                (filterDateSelections
                    ?.firstOrNull { it is DateSelection.Custom } as? DateSelection.Custom
                    ?: error("ApiError retrieving DateSelection as Custom from selected index."))
                    .run {
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

            val customIndex = filterDateSelections.indexOfFirst { it is DateSelection.Custom }
            copy(
                filterDateSelectionIndex = customIndex,
                filterDateSelections = filterDateSelections
                    .toMutableList()
                    .apply { set(customIndex, replacementCustom) }
            ).push()
        }
    }

    private fun onFilterDistanceChanged(event: FilterChanged.FilterDistanceChanged) {
        copyLastState {
            copy(
                filterDistanceSelectedStart = event.changedTo.start,
                filterDistanceSelectedEnd = event.changedTo.endInclusive,
                filterDistancePendingChangeStart = null,
                filterDistancePendingChangeEnd = null
            )
        }.push()
    }

    private fun onFilterDistancePendingChange(event: FilterDistancePendingChange) {
        pushStateCopy {
            if (event is FilterDistancePendingChange.FilterDistancePendingChangeShortest) {
                copy(filterDistancePendingChangeStart = event.changedTo)
            } else {
                copy(filterDistancePendingChangeEnd = event.changedTo)
            }
        }
    }

    private fun onFilterDistancePendingChangeConfirmed(event: FilterChanged.FilterDistancePendingChangeConfirmed) {
        pushStateCopy {
            val totalValueSmallest = filterDistanceTotalStart ?: 0.0
            val totalValueLargest = filterDistanceTotalEnd ?: Double.MAX_VALUE

            when (event) {
                is FilterChanged.FilterDistancePendingChangeConfirmed.StartConfirmed -> {
                    filterDistancePendingChangeStart?.let {
                        val coerceAtMost = filterDistanceSelectedEnd ?: totalValueLargest

                        copy(
                            filterDistanceSelectedStart = parseNumberFromStringUtils.parse(it)
                                .milesToMeters()
                                .coerceIn(totalValueSmallest.rangeTo(coerceAtMost)),
                            filterDistanceSelectedEnd = coerceAtMost,
                            filterDistancePendingChangeStart = null
                        )
                    }
                }
                is FilterChanged.FilterDistancePendingChangeConfirmed.EndConfirmed -> {
                    filterDistancePendingChangeEnd?.let {
                        val coerceAtLeast = filterDistanceSelectedStart ?: totalValueSmallest

                        copy(
                            filterDistanceSelectedStart = coerceAtLeast,
                            filterDistanceSelectedEnd = parseNumberFromStringUtils.parse(it)
                                .milesToMeters()
                                .coerceIn(coerceAtLeast.rangeTo(totalValueLargest)),
                            filterDistancePendingChangeEnd = null
                        )
                    }
                }
            }
        }
    }

    private fun onFilterTypeToggled(event: FilterChanged.FilterTypeToggled) {
        copyLastState {
            copy(filterTypes = filterTypes
                ?.toMutableMap()
                ?.apply { set(event.type, !get(event.type)!!) }
            )
        }.push()
    }

    private fun onNavigateUpClicked() {
        copyLastState { copy(dialogActive = EditArtDialogType.NAVIGATE_UP) }.push()
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
                val targetSize = sizeResolutionList[sizeResolutionListSelectedIndex]
                val filterRange = activitiesDateRangeUnixMs
                routeTo(
                    NavigateSaveArt(
                        activityTypes = filteredTypes,
                        backgroundColorArgb = styleBackgroundColor.color.toArgb(),
                        backgroundType = styleBackgroundType,
                        colorActivitiesArgb = styleActivities.color.toArgb(),
                        colorFontArgb = (styleFont ?: styleActivities).color.toArgb(),
                        filterAfterMs = filterRange?.first ?: Long.MIN_VALUE,
                        filterBeforeMs = filterRange?.last ?: Long.MAX_VALUE,
                        filterDistanceLessThanMeters = filterDistanceSelectedEnd
                            ?.roundToInt()
                            ?: Int.MAX_VALUE,
                        filterDistanceMoreThanMeters = filterDistanceSelectedStart
                            ?.roundToInt()
                            ?: Int.MIN_VALUE,
                        sizeHeightPx = targetSize.heightPx,
                        sizeWidthPx = targetSize.widthPx,
                        sortType = sortTypeSelected,
                        sortDirectionType = sortDirectionTypeSelected,
                        strokeWidthType = styleStrokeWidthType,
                        textLeft = LEFT.text,
                        textCenter = CENTER.text,
                        textRight = RIGHT.text,
                        textFontAssetPath = typeFontSelected.getAssetPath(
                            typeFontWeightSelected,
                            typeFontItalicized
                        ),
                        textFontSize = typeFontSizeSelected
                    )
                )
            }
        }
    }

    private fun onSizeChanged(event: SizeChanged) {
        copyLastState {
            copy(sizeResolutionListSelectedIndex = event.changedIndex)
        }.push()
    }

    private fun onSizeCustomChanged(event: SizeCustomChanged) {
        copyLastState {
            val customRange = sizeCustomMinPx..sizeCustomMaxPx
            copy(
                sizeResolutionListSelectedIndex = sizeResolutionList.indexOfFirst { it is Resolution.CustomResolution },
                sizeCustomOutOfBoundsWidth = if (event is SizeCustomChanged.WidthChanged) {
                    event.changedToPx.takeIf { it !in customRange }
                } else {
                    sizeCustomOutOfBoundsWidth
                },
                sizeCustomOutOfBoundsHeight = if (event is SizeCustomChanged.HeightChanged) {
                    event.changedToPx.takeIf { it !in customRange }
                } else {
                    sizeCustomOutOfBoundsHeight
                },
                sizeResolutionList = sizeResolutionList
                    .toMutableList()
                    .apply {
                        val tarIndex = indexOfFirst { it is Resolution.CustomResolution }
                        set(tarIndex, get(tarIndex).run {
                            val adjPx = event.changedToPx.coerceIn(customRange)
                            Resolution.CustomResolution(
                                widthPx = if (event is SizeCustomChanged.WidthChanged) adjPx else widthPx,
                                heightPx = if (event is SizeCustomChanged.HeightChanged) adjPx else heightPx
                            )
                        })
                    }
            )
        }.push()
    }

    private fun onSizeRotated(event: SizeRotated) {
        copyLastState {
            copy(
                sizeResolutionList = sizeResolutionList
                    .toMutableList()
                    .apply {
                        val prevRes = get(event.rotatedIndex)
                        val adjRes = (prevRes as? Resolution.RotatingResolution
                            ?: error("Rotated a non-rotating resolution."))
                            .copyWithRotation()
                        set(event.rotatedIndex, adjRes)
                    }
            )
        }.push()
    }

    private fun onSortDirectionChanged(event: SortDirectionChanged) {
        copyLastState { copy(sortDirectionTypeSelected = event.changedTo) }.push()
    }

    private fun onSortTypeChanged(event: SortTypeChanged) {
        copyLastState { copy(sortTypeSelected = event.changedTo) }.push()
    }

    private fun onStyleBackgroundTypeChanged(event: StyleBackgroundTypeChanged) {
        copyLastState { copy(styleBackgroundType = event.changedTo) }.push()
    }

    private fun onStyleColorActivitiesChanged(event: StyleColorActivitiesChanged) {
        pushStateCopy {
            copy(
                styleActivities = styleActivities.copyWithChange(
                    colorType = event.colorType,
                    changedTo = event.changedTo
                )
            )
        }
    }

    private fun onStyleColorsBackgroundChanged(event: StyleColorsBackgroundChanged) {
        pushStateCopy {
            copy(
                styleBackgroundColor = styleBackgroundColor.copyWithChange(
                    colorType = event.changedColorType,
                    changedTo = event.changedTo
                )
            )
        }
    }

    private fun onStyleColorFontChanged(event: StyleColorFontChanged) {
        pushStateCopy {
            copy(
                styleFont = (styleFont ?: styleActivities).copyWithChange(
                    colorType = event.colorType,
                    changedTo = event.changedTo
                )
            )
        }
    }

    private fun onStyleColorFontUseCustomChanged(event: StyleColorFontUseCustomChanged) {
        copyLastState {
            copy(styleFont = if (event.useCustom) styleFont ?: styleActivities else null)
        }.push()
    }

    private fun onStylesStrokeWidthChanged(event: StylesStrokeWidthChanged) {
        (lastPushedState as? Standby)?.run {
            copy(styleStrokeWidthType = event.changedTo)
        }?.push()
    }

    private fun onTypeCustomTextChanged(event: TypeCustomTextChanged) {
        val newText: String? =
            event.changedTo.takeIf {
                (state?.typeMaximumCustomTextLength?.compareTo(it.length) ?: -1) >= 0
            }
        copyLastState {
            when (event.section) {
                LEFT -> copy(typeLeftCustomText = newText ?: typeLeftCustomText)
                CENTER -> copy(typeCenterCustomText = newText ?: typeCenterCustomText)
                RIGHT -> copy(typeRightCustomText = newText ?: typeRightCustomText)
            }
        }.push()
    }

    private fun onTypeFontChanged(event: TypeFontChanged) {
        copyLastState {
            event.changedTo.run {
                copy(
                    typeFontItalicized = typeFontItalicized && isItalic,
                    typeFontSelected = this,
                    typeFontWeightSelected = if (fontWeightTypes.contains(typeFontWeightSelected)) {
                        typeFontWeightSelected
                    } else {
                        FontWeightType.REGULAR
                    },
                )
            }
        }.push()
    }

    private fun onTypeFontSizeChanged(event: TypeFontSizeChanged) {
        copyLastState { copy(typeFontSizeSelected = event.changedTo) }.push()
    }

    private fun onTypeSelectionChanged(event: TypeSelectionChanged) {
        copyLastState {
            when (event.section) {
                LEFT -> copy(typeLeftSelected = event.typeSelected)
                CENTER -> copy(typeCenterSelected = event.typeSelected)
                RIGHT -> copy(typeRightSelected = event.typeSelected)
            }
        }.push()
    }

    private fun onTypeFontWeightChanged(event: TypeFontWeightChanged) {
        copyLastState { copy(typeFontWeightSelected = event.changedTo) }.push()
    }

    private fun onTypeFontItalicChanged(event: TypeFontItalicChanged) {
        copyLastState { copy(typeFontItalicized = event.changedTo) }.push()
    }

    /** @return Copy of an reflecting a [StylesColorChanged] change event
     *  to a [ColorWrapper]. **/
    private fun ColorWrapper.copyWithChange(
        colorType: ColorType,
        changedTo: Float
    ): ColorWrapper {
        return when (colorType) {
            ALPHA -> copy(
                alpha = changedTo.coerceIn(ColorWrapper.RATIO_RANGE),
                outOfBoundsAlpha = changedTo
                    .takeIf { !ColorWrapper.RATIO_RANGE.contains(it) }
            )
            BLUE -> copy(
                blue = changedTo.coerceIn(ColorWrapper.RATIO_RANGE),
                outOfBoundsBlue = changedTo
                    .takeIf { !ColorWrapper.RATIO_RANGE.contains(it) }
            )
            GREEN -> copy(
                green = changedTo.coerceIn(ColorWrapper.RATIO_RANGE),
                outOfBoundsGreen = changedTo
                    .takeIf { !ColorWrapper.RATIO_RANGE.contains(it) }
            )
            RED -> copy(
                red = changedTo.coerceIn(ColorWrapper.RATIO_RANGE),
                outOfBoundsRed = changedTo
                    .takeIf { !ColorWrapper.RATIO_RANGE.contains(it) }
            )
        }
    }

    private fun updateBitmap() {
        println("Here, update bitmap invoked, final filtered is ${activitiesFiltered.size} size")
        imageProcessingDispatcher.cancelChildren()
        viewModelScope.launch(imageProcessingDispatcher) {
            copyLastState {
                val bitmap = visualizationUtils.createBitmap(
                    activities = activitiesFiltered, // todo...
                    backgroundType = styleBackgroundType,
                    backgroundColorArgb = styleBackgroundColor.color.toArgb(),
                    colorActivitiesArgb = styleActivities.color.toArgb(),
                    colorFontArgb = (styleFont ?: styleActivities).color.toArgb(),
                    strokeWidth = styleStrokeWidthType,
                    bitmapSize = imageSizeUtils.sizeToMaximumSize(
                        actualSize = sizeResolutionList[sizeResolutionListSelectedIndex].run {
                            Size(widthPx, heightPx)
                        },
                        maximumSize = Size(
                            PREVIEW_BITMAP_MAX_SIZE_WIDTH_PX,
                            PREVIEW_BITMAP_MAX_SIZE_HEIGHT_PX
                        )
                    ),
                    fontAssetPath = typeFontSelected.getAssetPath(
                        fontWeightType = typeFontWeightSelected,
                        italicized = typeFontItalicized
                    ),
                    fontSize = typeFontSizeSelected,
                    isPreview = true,
                    sortType = sortTypeSelected,
                    sortDirectionType = sortDirectionTypeSelected,
                    textLeft = LEFT.text,
                    textCenter = CENTER.text,
                    textRight = RIGHT.text
                )
                copyLastState { copy(bitmap = bitmap) }
            }.push()
        }
    }

    private inline fun copyLastState(block: Standby.() -> Standby): Standby {
        return (lastPushedState as? Standby)?.run(block) ?: error("Last state was not standby")
    }

    private inline fun pushStateCopy(block: Standby.() -> Standby?) {
        (lastPushedState as? Standby)
            ?.run(block)
            ?.push()
    }

    private inline fun withLastState(block: Standby.() -> Unit) {
        (lastPushedState as? Standby)?.run(block)
    }

    private val EditArtTypeSection.text: String?
        get() {
            return state?.run {
                val typeCustomText: Pair<EditArtTypeType, String> = when (this@text) {
                    CENTER -> Pair(typeCenterSelected, typeCenterCustomText)
                    LEFT -> Pair(typeLeftSelected, typeLeftCustomText)
                    RIGHT -> Pair(typeRightSelected, typeRightCustomText)
                }

                when (typeCustomText.first) {
                    NONE -> null
                    DISTANCE_MILES -> activitiesSummedDistance.meterToMilesStr()
                    DISTANCE_KILOMETERS -> activitiesSummedDistance.meterToKilometerStr()
                    CUSTOM -> typeCustomText.second.takeIf { it.isNotBlank() }
                }
            }
        }

    private fun Double.meterToMilesStr(): String = "${(this * 0.000621371192).roundToInt()} mi"
    private fun Double.meterToKilometerStr(): String = "${(this / 1000f).roundToInt()} km"
    private fun Double.milesToMeters(): Double = this / 0.000621371192

    private val state: Standby? get() = lastPushedState as? Standby
}