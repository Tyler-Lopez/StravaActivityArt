package com.activityartapp.presentation.editArtScreen

import android.util.Range
import android.util.Size
import androidx.compose.ui.graphics.toArgb
import androidx.core.util.rangeTo
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
import com.activityartapp.presentation.editArtScreen.StyleType.*
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeSection
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeSection.*
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeType
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeType.*
import com.activityartapp.util.ActivityFilterUtils
import com.activityartapp.util.ImageSizeUtils
import com.activityartapp.util.TimeUtils
import com.activityartapp.util.VisualizationUtils
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
) : BaseRoutingViewModel<EditArtViewState, EditArtViewEvent, MainDestination>() {

    companion object {
        private const val DEFAULT_ACTIVITY_TYPE_SELECTION = true
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

    /** Updates [activitiesFilteredByFilterType] for a given [EditArtFilterType].
     * Designates which activities this particular filter type is in-charge of filtering. **/
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
            if (this@updateFilteredActivities == filterFinal) pushUpdatedDistancesToView()
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
                            filteredActivities,
                            filterDateSelections
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
                    val newRangeSelected = newRangeTotal?.let {
                        activityFilterUtils.getAdjustedSelectedDistancesInRange(
                            it,
                            filterDistanceSelectedStart,
                            filterDistanceSelectedEnd
                        )
                    }
                    copy(
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
        }.push()
    }

    private fun pushUpdatedDistancesToView() {
        copyLastState {
            copy(typeActivitiesDistanceMetersSummed = activitiesFiltered
                .sumOf { it.distance }
                .roundToInt()
            )
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
                    /** Otherwise, simply initialize filtered activities for each type as all activities and
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
            is FilterChanged.FilterTypeToggled -> onFilterTypeToggled(event)
        }

        /** Run all operations which require many linear scans of activities off of the main thread **/
        viewModelScope.launch(activitiesProcessingDispatcher) {
            event.filterType.apply {
                updateFilteredActivities()
                forEachNextFilterType {
                    it.updateFilters()
                    it.updateFilteredActivities()
                }
            }
            updateBitmap()
            savedStateHandle[STANDBY_SAVE_STATE_KEY] = (lastPushedState as? Standby)
        }
    }

    private fun onDialogNavigateUpCancelled() {
        copyLastState { copy(dialogNavigateUpActive = false) }.push()
    }

    private fun onDialogNavigateUpConfirmed() {
        copyLastState { copy(dialogNavigateUpActive = false) }.push()
        viewModelScope.launch { routeTo(NavigateUp) }
    }

    private fun onFilterDateSelectionChanged(event: FilterChanged.FilterDateSelectionChanged) {
        copyLastState { copy(filterDateSelectionIndex = event.index) }.push()
    }

    private fun onFilterDateCustomChanged(event: FilterChanged.FilterDateCustomChanged) {
        withLastState {
            val replacementCustom: DateSelection.Custom =
                (filterDateSelections
                    ?.get(filterDateSelectionIndex) as? DateSelection.Custom
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

            copy(
                filterDateSelections = filterDateSelections
                    .toMutableList()
                    .apply { set(filterDateSelectionIndex, replacementCustom) }
            ).push()
        }
    }

    private fun onFilterDistanceChanged(event: FilterChanged.FilterDistanceChanged) {
        copyLastState {
            copy(
                filterDistanceSelectedStart = event.changedTo.start,
                filterDistanceSelectedEnd = event.changedTo.endInclusive
            )
        }.push()
    }

    private fun onFilterTypeToggled(event: FilterChanged.FilterTypeToggled) {
        copyLastState {
            copy(filterTypes = filterTypes
                ?.toMutableMap()
                ?.apply { set(event.type, !get(event.type)!!) }
            )
        }.push()
    }

    private fun onMakeFullscreenClicked() {
        // Todo
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
                val filterRange = activitiesDateRangeUnixMs
                routeTo(
                    NavigateSaveArt(
                        activityTypes = filteredTypes,
                        colorActivitiesArgb = styleActivities.color.toArgb(),
                        colorBackgroundArgb = styleBackground.color.toArgb(),
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

    private fun onSizeCustomChangeDone() {
        // No-op, this event is necessary as it is Art Mutating where SizeCustomChanged is not.
    }

    private fun onSizeCustomChanged(event: SizeCustomChanged) {
        copyLastState {
            copy(
                sizeResolutionList = sizeResolutionList
                    .toMutableList()
                    .apply {
                        val tarIndex = indexOfFirst { it is Resolution.CustomResolution }
                        set(tarIndex, get(tarIndex).run {
                            Resolution.CustomResolution(
                                widthPx = if (event is SizeCustomChanged.WidthChanged) event.changedToPx else widthPx,
                                heightPx = if (event is SizeCustomChanged.HeightChanged) event.changedToPx else heightPx
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

    private fun onStylesColorChanged(event: StylesColorChanged) {
        copyLastState {
            event.run {
                when (styleType) {
                    BACKGROUND -> copy(styleBackground = styleBackground.copyWithEvent(event))
                    ACTIVITIES -> copy(styleActivities = styleActivities.copyWithEvent(event))
                    FONT -> copy(
                        styleFont = (styleFont ?: styleActivities).copyWithEvent(event)
                    )
                }
            }
        }.push()
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
        println("Here, update bitmap invoked, final filtered is ${activitiesFiltered.size} size")
        imageProcessingDispatcher.cancelChildren()
        viewModelScope.launch(imageProcessingDispatcher) {
            copyLastState {
                val bitmap = visualizationUtils.createBitmap(
                    activities = activitiesFiltered, // todo...
                    colorActivitiesArgb = styleActivities.color.toArgb(),
                    colorBackgroundArgb = styleBackground.color.toArgb(),
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
                    DISTANCE_MILES -> activitiesFiltered.sumOf { it.distance }.meterToMilesStr()
                    DISTANCE_KILOMETERS -> activitiesFiltered.sumOf { it.distance }
                        .meterToKilometerStr()
                    CUSTOM -> typeCustomText.second.takeIf { it.isNotBlank() }
                }
            }
        }

    private fun Double.meterToMilesStr(): String = "${(this * 0.000621371192).roundToInt()} mi"
    private fun Double.meterToKilometerStr(): String = "${(this / 1000f).roundToInt()} km"
    private fun <T : Comparable<T>> T?.rangeToOrNull(end: T?): Range<T>? =
        end?.let { this?.rangeTo(it) }

    private fun List<Long>.asRangeOrNullIfEmpty(): LongProgression? {
        return maxOrNull()?.let { max -> minOrNull()?.rangeTo(max) }
    }

    private val state: Standby? get() = lastPushedState as? Standby
}