package com.activityartapp.presentation.editArtScreen

import android.os.Parcelable
import android.util.Range
import android.util.Size
import androidx.compose.ui.graphics.toArgb
import androidx.core.util.rangeTo
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.MainDestination.NavigateSaveArt
import com.activityartapp.presentation.MainDestination.NavigateUp
import com.activityartapp.presentation.editArtScreen.*
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
import com.activityartapp.util.*
import com.activityartapp.util.enums.FontWeightType
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject
import kotlin.math.roundToInt


@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagerApi::class)
@HiltViewModel
class EditArtViewModel @Inject constructor(
    activitiesFromCacheUseCase: GetActivitiesFromCacheUseCase,
    private val imageSizeUtils: ImageSizeUtils,
    private val timeUtils: TimeUtils,
    private val visualizationUtils: VisualizationUtils,
    private val savedStateHandle: SavedStateHandle,
) : BaseRoutingViewModel<EditArtViewState, EditArtViewEvent, MainDestination>() {

    companion object {
        private const val DEFAULT_SELECTION = true
        private const val PREVIEW_BITMAP_MAX_SIZE_WIDTH_PX = 2000
        private const val PREVIEW_BITMAP_MAX_SIZE_HEIGHT_PX = 2000
        private const val CUSTOM_TEXT_MAXIMUM_LENGTH = 30

        private const val STANDBY_SAVE_STATE_KEY = "StandbySaveState"
        private const val EDIT_ART_VIEW_MODEL_SAVE_STATE_KEY = "EditArtViewModel"
    }

    /** All activities cached in Singleton memory **/
    private val activities: List<Activity> = activitiesFromCacheUseCase()

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
                    is DateSelection.Custom -> (dateSelected ?: dateTotal)
                        .run { first.rangeTo(last) }
                }
            }
        }


    /** Updates [activitiesFilteredByFilterType] for a given [EditArtFilterType].
     * Designates which activities this particular filter type is in-charge of filtering. **/
    private fun EditArtFilterType.updateFilteredActivities() {
        withLastState {
            val prevActivities = activitiesFilteredByFilterType[lastFilter] ?: activities
            val activitiesDateRangeUnixMs = activitiesDateRangeUnixMs
            val filteredActivities = prevActivities.filter { activity ->
                when (this@updateFilteredActivities) {
                    DATE -> timeUtils
                        .iso8601StringToUnixMillisecond(activity.iso8601LocalDate)
                        .let {
                            activitiesDateRangeUnixMs?.let { range ->
                                it >= range.first && it <= range.last
                            } ?: true
                        }
                    TYPE -> {
                        println("Here evaluating type for ${activity.type}")
                        println(
                            "Evaluates in my thing ${state?.filterTypes} to ${
                                state?.filterTypes?.get(
                                    activity.type
                                )
                            }"
                        )
                        state?.filterTypes?.get(activity.type) ?: true
                    }
                    DISTANCE -> {
                        val rng =
                            filterDistanceSelectedStart.rangeToOrNull(filterDistanceSelectedEnd)
                                ?: Double.MIN_VALUE.rangeTo(Double.MAX_VALUE)
                        activity.distance in rng
                    }
                }
            }
            activitiesFilteredByFilterType[this@updateFilteredActivities] = filteredActivities
        }
    }

    /** Updates the corresponding [EditArtFilterType] property of the [FilterChoicesState] object to
     * reflect the user's current choices. **/
    private fun EditArtFilterType.updateFilters() {
        val filteredActivities = (activitiesFilteredByFilterType[lastFilter] ?: activities)
        withLastState {
            when (this@updateFilters) {
                DATE -> {
                    filteredActivities
                        .map { SECONDS.toMillis(timeUtils.iso8601StringToUnixSecond(it.iso8601LocalDate)) }
                        .asRangeOrNullIfEmpty()
                        ?.let {
                            val newDateSelections = mutableListOf<DateSelection>(DateSelection.All)
                                .apply {
                                    addAll(filteredActivities
                                        .map { timeUtils.iso8601StringToYearMonth(it.iso8601LocalDate).first }
                                        .distinct()
                                        .map { DateSelection.Year(it) }
                                        .toMutableList<DateSelection>()
                                        .apply {
                                            add(
                                                DateSelection.Custom(
                                                    dateSelectedStart = null,
                                                    dateSelectedEnd = null,
                                                    dateTotalStart = it.first,
                                                    dateTotalEnd = it.last
                                                )
                                            )
                                        })
                                }
                            copy(
                                filterDateSelections = newDateSelections,
                                // Todo, more readable
                                filterDateSelectionIndex = if (filterDateSelectionUnset) {
                                    0
                                } else {
                                    filterDateSelectionIndex
                                }
                            )
                        }
                }
                TYPE -> copy(
                    filterTypes = filteredActivities
                        .distinctBy(Activity::type)
                        .map(Activity::type)
                        .associateWith {
                            filterTypes?.get(it) ?: DEFAULT_SELECTION
                        }
                )
                DISTANCE -> {
                    val distances = filteredActivities.map { it.distance }
                    val distanceShortest = distances.minOrNull()
                    val distanceLongest = distances.maxOrNull()
                    val filterDistanceSelected = filterDistanceSelectedEnd?.let {
                        filterDistanceSelectedStart?.rangeTo(it)
                    }

                    val adjStart = filterDistanceSelected?.lower?.takeIf {
                        it >= (distanceShortest ?: Double.MAX_VALUE)
                    }
                    val adjEnd = filterDistanceSelected?.upper?.takeIf {
                        it <= (distanceLongest ?: Double.MIN_VALUE)
                    }

                    copy(
                        filterDistanceSelectedStart = adjStart,
                        filterDistanceSelectedEnd = adjEnd,
                        filterDistanceTotalStart = distanceShortest,
                        filterDistanceTotalEnd = distanceLongest
                    )
                }
            }?.push()
        }
        savedStateHandle[EDIT_ART_VIEW_MODEL_SAVE_STATE_KEY] = state
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

    private val imageProcessingDispatcher by lazy { Dispatchers.Default.limitedParallelism(1) }

    init {
        Loading().push()
        viewModelScope.launch {

            /** If there was not previously-saved state, the properties of the just-constructed
             * state will be populated here. **/
            EditArtFilterType.values().forEach {
                activitiesFilteredByFilterType[it] = activities
            }

            /** Push either a previously-saved (if available) or constructed Standby state **/
            val prevState: Standby? = savedStateHandle[STANDBY_SAVE_STATE_KEY]
            (prevState ?: Standby()).push()

            // todo, believe issue is with how things are init with null and such... look into, see how type doesnt do anyuthing but distance do es
            /** If there was a saved state, now that we've pushed Standby update filtered activities to reflect
             * the various saved filters. **/
            EditArtFilterType.values().forEach {
                if (prevState != null) {
                    println("Prev state was not null, activity types are ${state?.filterTypes}")
                    it.updateFilteredActivities()
                } else {
                    it.updateFilters()
                }
                it.pushUpdatedFilteredActivityCountToView()
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
        updateBitmap()
        savedStateHandle[STANDBY_SAVE_STATE_KEY] = (lastPushedState as? Standby)
    }

    private fun onFilterChangeEvent(event: FilterChanged) {
        when (event) {
            is FilterChanged.FilterDateSelectionChanged -> onFilterDateSelectionChanged(event)
            is FilterChanged.FilterDateCustomChanged -> onFilterDateCustomChanged(event)
            is FilterChanged.FilterDistanceChanged -> onFilterDistanceChanged(event)
            is FilterChanged.FilterTypeToggled -> onFilterTypeToggled(event)
        }
        event.filterType.apply {
            updateFilteredActivities()
            pushUpdatedFilteredActivityCountToView()
            forEachNextFilterType {
                it.updateFilters()
                //it.pushUpdatedFiltersToView()
                it.updateFilteredActivities()
                it.pushUpdatedFilteredActivityCountToView()
            }
        }
        copyLastState {
            copy(typeActivitiesDistanceMetersSummed = activitiesFiltered
                .sumOf { it.distance }
                .roundToInt())
        }.push()
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
                        val selectedEnd = dateSelected?.last ?: dateTotal.last
                        val selectedStart = dateSelected?.first ?: dateTotal.first
                        val totalEnd = dateTotal.last

                        copy(
                            dateSelectedStart = when (event) {
                                is FilterChanged.FilterDateCustomChanged.FilterAfterChanged -> {
                                    event.changedTo.coerceAtMost(totalEnd)
                                }
                                is FilterChanged.FilterDateCustomChanged.FilterBeforeChanged -> {
                                    selectedStart
                                }
                            },
                            dateSelectedEnd = when (event) {
                                is FilterChanged.FilterDateCustomChanged.FilterAfterChanged -> {
                                    selectedEnd
                                }
                                is FilterChanged.FilterDateCustomChanged.FilterBeforeChanged -> {
                                    event.changedTo.coerceAtMost(totalEnd)
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
            println("Here, on filter type toggled, filter types was $filterTypes")
            copy(filterTypes = filterTypes
                ?.toMutableMap()
                ?.apply { set(event.type, !get(event.type)!!) }
            )
        }.push()
        println("Hi, now my thing is ${state?.filterTypes}")
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
                        activityTypes = filterTypes?.entries?.filter { it.value }?.map { it.key }
                            ?: listOf(),
                        colorActivitiesArgb = styleActivities.color.toArgb(),
                        colorBackgroundArgb = styleBackground.color.toArgb(),
                        colorFontArgb = (styleFont ?: styleActivities).color.toArgb(),
                        filterAfterMs = filterRange?.first ?: Long.MIN_VALUE,
                        filterBeforeMs = filterRange?.last ?: Long.MAX_VALUE,
                        filterDistanceLessThan = filterDistanceSelectedEnd ?: Double.MAX_VALUE,
                        filterDistanceMoreThan = filterDistanceSelectedStart
                            ?: Double.MIN_VALUE,
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
            event.changedTo.takeIf { it.length <= CUSTOM_TEXT_MAXIMUM_LENGTH }
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
        imageProcessingDispatcher.cancelChildren()
        viewModelScope.launch(imageProcessingDispatcher) {
            copyLastState {
                val bitmap = visualizationUtils.createBitmap(
                    activities = activitiesFilteredByFilterType[filterFinal]
                        ?: listOf(), // todo...
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

    private fun getStandbyState() =
        lastPushedState as? Standby ?: error("Last pushed state was not standby")

    private val EditArtTypeSection.text: String?
        get() {
            return getStandbyState().run {
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
    private fun Double?.rangeToOrNull(end: Double?): Range<Double>? =
        end?.let { this?.rangeTo(it) }

    private fun List<Long>.asRangeOrNullIfEmpty(): LongProgression? {
        return maxOrNull()?.let { max -> minOrNull()?.rangeTo(max) }
    }

    private val state: Standby?
        get() = lastPushedState as? Standby
}