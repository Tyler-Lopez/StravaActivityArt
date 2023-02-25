package com.activityartapp.presentation.editArtScreen

import android.util.Size
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.useCase.activities.GetActivitiesFromDisk
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
    private val getActivitiesFromDisk: GetActivitiesFromDisk,
    private val activityFilterUtils: ActivityFilterUtils,
    private val imageSizeUtils: ImageSizeUtils,
    private val timeUtils: TimeUtils,
    private val visualizationUtils: VisualizationUtils,
    private val parseNumberFromStringUtils: ParseNumberFromStringUtils,
    private val ssh: SavedStateHandle
) : BaseRoutingViewModel<EditArtViewState, EditArtViewEvent, MainDestination>() {

    companion object {
        private const val PREVIEW_BITMAP_MAX_SIZE_WIDTH_PX = 2000
        private const val PREVIEW_BITMAP_MAX_SIZE_HEIGHT_PX = 2000

        private const val STANDBY_SAVE_STATE_KEY = "StandbySaveState"

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
        viewModelScope.launch(Dispatchers.Default) {
            activities = getActivitiesFromDisk(athleteId)

            /** Push either a previously-saved (if available) or constructed Standby state **/
            val prevState: Standby? = ssh[STANDBY_SAVE_STATE_KEY]
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
        }
    }

    private fun onArtMutatingEvent(event: ArtMutatingEvent) {
        when (event) {
            is FilterChanged -> onFilterChangeEvent(event)
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
            ssh[STANDBY_SAVE_STATE_KEY] = (lastPushedState as? Standby)
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
            ssh[STANDBY_SAVE_STATE_KEY] = (lastPushedState as? Standby)
        }
    }

    private fun onClickedRemoveGradientColor(event: ClickedRemoveGradientColor) {
        pushStateCopy { copy(dialogActive = EditArtDialog.ConfirmDeleteGradientColor(event.removedIndex)) }
    }

    private fun onClickedInfoCheckeredBackground() {
        pushStateCopy { copy(dialogActive = EditArtDialog.InfoCheckeredBackground) }
    }

    private fun onClickedInfoGradientBackground() {
        pushStateCopy { copy(dialogActive = EditArtDialog.InfoGradientBackground) }
    }

    private fun onClickedInfoTransparentBackground() {
        pushStateCopy { copy(dialogActive = EditArtDialog.InfoTransparent) }
    }

    private fun onDialogDismissed() {
        pushStateCopy { copy(dialogActive = EditArtDialog.None) }
    }

    private fun onDialogNavigateUpConfirmed() {
        copyLastState { copy(dialogActive = EditArtDialog.None) }.push()
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
        copyLastState { copy(dialogActive = EditArtDialog.NavigateUp) }.push()
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
                        athleteId = athleteId,
                        backgroundColorsArgb = styleBackgroundList
                            .take(styleBackgroundGradientColorCount)
                            .map { it.toColorArgb() }, // TODO
                        backgroundAngleType = styleBackgroundAngleType,
                        backgroundType = styleBackgroundType,
                        colorActivitiesArgb = styleActivities.toColorArgb(),
                        colorFontArgb = (styleFont ?: styleActivities).toColorArgb(),
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

    private fun onStyleColorPendingChanged(event: StyleColorPendingChanged) {
        pushStateCopy {
            when (event.style) {
                is StyleIdentifier.Activities -> copy(
                    styleActivities = styleActivities.copyPendingChange(
                        colorType = event.colorType,
                        changedTo = event.changedTo
                    )
                )
                is StyleIdentifier.Background -> {
                    val color = styleBackgroundList[event.style.index]
                    val newBackgroundList = styleBackgroundList.toMutableList()
                    newBackgroundList[event.style.index] = color.copyPendingChange(
                        colorType = event.colorType,
                        changedTo = event.changedTo
                    )
                    copy(styleBackgroundList = newBackgroundList)
                }
                is StyleIdentifier.Font -> copy(
                    styleFont = (styleFont ?: styleActivities).copyPendingChange(
                        colorType = event.colorType,
                        changedTo = event.changedTo
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
        pushStateCopy {
            val customRes = sizeResolutionList[event.customIndex] as Resolution.CustomResolution
            val newSizeResolutionList = sizeResolutionList.toMutableList()
            newSizeResolutionList[event.customIndex] = if (event.heightChanged) {
                customRes.copy(heightPx = event.changedToPx, pendingHeight = null)
            } else {
                customRes.copy(widthPx = event.changedToPx, pendingWidth = null)
            }

            copy(
                sizeResolutionListSelectedIndex = event.customIndex,
                sizeResolutionList = newSizeResolutionList
            )
        }
    }

    private fun onSizeCustomPendingChangeConfirmed(event: SizeCustomPendingChangeConfirmed) {
        pushStateCopy {
            val customRes = sizeResolutionList[event.customIndex] as Resolution.CustomResolution
            val range = customRes.sizeRangePx
            val newSizeResolutionList = sizeResolutionList.toMutableList()
            newSizeResolutionList[event.customIndex] = customRes.run {
                copy(
                    widthPx = pendingWidth?.let {
                        parseNumberFromStringUtils.parse(it).toInt().coerceIn(range)
                    } ?: widthPx,
                    heightPx = pendingHeight?.let {
                        parseNumberFromStringUtils.parse(it).toInt().coerceIn(range)
                    } ?: heightPx,
                    pendingWidth = null,
                    pendingHeight = null
                )
            }

            copy(
                sizeResolutionListSelectedIndex = event.customIndex,
                sizeResolutionList = newSizeResolutionList
            )
        }
    }

    private fun onSizeCustomPendingChanged(event: SizeCustomPendingChanged) {
        pushStateCopy {
            copy(
                sizeResolutionList = sizeResolutionList
                    .toMutableList()
                    .apply {
                        val tarIndex = indexOfFirst { it is Resolution.CustomResolution }
                        set(tarIndex, (get(tarIndex) as Resolution.CustomResolution).run {
                            copy(
                                pendingWidth = if (event is SizeCustomPendingChanged.WidthChanged) event.changedTo else pendingWidth,
                                pendingHeight = if (event is SizeCustomPendingChanged.HeightChanged) event.changedTo else pendingHeight
                            )
                        })
                    }
            )
        }
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

    private fun onStyleBackgroundColorAdded() {
        withLastState {
            if (styleBackgroundGradientColorCount >= EditArtViewState.MAX_GRADIENT_BG_COLORS) {
                return
            }
            copy(styleBackgroundGradientColorCount = styleBackgroundGradientColorCount + 1).push()
        }
    }

    private fun onStyleBackgroundColorRemoveConfirmed() {
        withLastState {
            if (styleBackgroundGradientColorCount <= EditArtViewState.MIN_GRADIENT_BG_COLORS) {
                return
            }
            val removedIndex = (dialogActive as? EditArtDialog.ConfirmDeleteGradientColor)
                ?.toDeleteIndex
                ?: return

            val newBackgroundList = styleBackgroundList.toMutableList()
            for (i in removedIndex until newBackgroundList.lastIndex) {
                val newColor = newBackgroundList[i + 1]
                val replacingAt = newBackgroundList[i]
                newBackgroundList[i] = replacingAt.apply {
                    red = newColor.red
                    green = newColor.green
                    blue = newColor.blue
                }
            }
            copy(
                dialogActive = EditArtDialog.None,
                styleBackgroundGradientColorCount = styleBackgroundGradientColorCount - 1,
                styleBackgroundList = newBackgroundList
            ).push()
        }
    }

    private fun onStyleBackgroundTypeChanged(event: StyleBackgroundTypeChanged) {
        pushStateCopy {
            if (styleBackgroundType == event.changedTo) {
                return@pushStateCopy null
            }
            copy(styleBackgroundType = event.changedTo)
        }
    }

    private fun onStyleColorChanged(event: StyleColorChanged) {
        pushStateCopy {
            when (event.style) {
                is StyleIdentifier.Activities -> copy(
                    styleActivities = styleActivities.copyWithChange(
                        colorType = event.colorType,
                        changedTo = event.changedTo
                    )
                )
                is StyleIdentifier.Background -> {
                    val color = styleBackgroundList[event.style.index]
                    val newBackgroundList = styleBackgroundList.toMutableList()
                    newBackgroundList[event.style.index] = color.copyWithChange(
                        colorType = event.colorType,
                        changedTo = event.changedTo
                    )
                    copy(styleBackgroundList = newBackgroundList)
                }
                is StyleIdentifier.Font -> copy(
                    styleFont = (styleFont ?: styleActivities).copyWithChange(
                        colorType = event.colorType,
                        changedTo = event.changedTo
                    )
                )
            }
        }
    }

    private fun onStyleColorPendingChangeConfirmed(event: StyleColorPendingChangeConfirmed) {
        pushStateCopy {
            when (event.style) {
                is StyleIdentifier.Activities -> copy(styleActivities = styleActivities.confirmPendingChanges())
                is StyleIdentifier.Background -> {
                    val color = styleBackgroundList[event.style.index]
                    val newBackgroundList = styleBackgroundList.toMutableList()
                    newBackgroundList[event.style.index] = color.confirmPendingChanges()
                    copy(styleBackgroundList = newBackgroundList)
                }
                is StyleIdentifier.Font -> copy(styleFont = styleFont?.confirmPendingChanges())
            }
        }
    }

    private fun onStyleColorFontUseCustomChanged(event: StyleColorFontUseCustomChanged) {
        copyLastState {
            copy(styleFont = if (event.useCustom) styleFont ?: styleActivities else null)
        }
    }

    private fun onStyleGradientAngleTypeChanged(event: StyleGradientAngleTypeChanged) {
        pushStateCopy { copy(styleBackgroundAngleType = event.changedTo) }
    }

    private fun onStyleStrokeWidthChanged(event: StyleStrokeWidthChanged) {
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
        imageProcessingDispatcher.cancelChildren()
        viewModelScope.launch(imageProcessingDispatcher) {
            copyLastState {
                val bitmap = visualizationUtils.createBitmap(
                    activities = activitiesFiltered,
                    backgroundAngleType = styleBackgroundAngleType,
                    backgroundType = styleBackgroundType,
                    backgroundColorsArgb = styleBackgroundList
                        .take(styleBackgroundGradientColorCount)
                        .map { it.toColorArgb() }, // TODO
                    colorActivitiesArgb = styleActivities.toColorArgb(),
                    colorFontArgb = (styleFont ?: styleActivities).toColorArgb(),
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