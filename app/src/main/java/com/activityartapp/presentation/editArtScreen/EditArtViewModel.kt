package com.activityartapp.presentation.editArtScreen

import android.location.Location
import android.util.Size
import androidx.compose.foundation.ScrollState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.viewModelScope
import com.activityartapp.architecture.BaseRoutingViewModel
import com.activityartapp.domain.models.Activity
import com.activityartapp.domain.models.ResolutionListFactory
import com.activityartapp.domain.models.fullName
import com.activityartapp.domain.use_case.activities.GetActivitiesFromCacheUseCase
import com.activityartapp.domain.use_case.athlete.GetAthleteFromLocalUseCase
import com.activityartapp.presentation.MainDestination
import com.activityartapp.presentation.MainDestination.NavigateSaveArt
import com.activityartapp.presentation.MainDestination.NavigateUp
import com.activityartapp.presentation.editArtScreen.ColorType.*
import com.activityartapp.presentation.editArtScreen.EditArtFilterType.*
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.*
import com.activityartapp.presentation.editArtScreen.EditArtViewEvent.ArtMutatingEvent.*
import com.activityartapp.presentation.editArtScreen.EditArtViewState.Loading
import com.activityartapp.presentation.editArtScreen.EditArtViewState.Standby
import com.activityartapp.presentation.editArtScreen.StrokeWidthType.MEDIUM
import com.activityartapp.presentation.editArtScreen.StyleType.*
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeSection
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeSection.*
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeType
import com.activityartapp.presentation.editArtScreen.subscreens.type.EditArtTypeType.*
import com.activityartapp.util.*
import com.activityartapp.util.enums.FontType
import com.activityartapp.util.enums.FontWeightType
import com.activityartapp.presentation.editArtScreen.*
import com.activityartapp.util.FontSizeType
import com.activityartapp.util.ImageSizeUtils
import com.activityartapp.util.TimeUtils
import com.activityartapp.util.VisualizationUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.maps.android.PolyUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.reflect.KProperty1

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagerApi::class)
@HiltViewModel
class EditArtViewModel @Inject constructor(
    private val athleteFromLocalUseCase: GetAthleteFromLocalUseCase,
    private val activitiesFromCacheUseCase: GetActivitiesFromCacheUseCase,
    private val imageSizeUtils: ImageSizeUtils,
    private val timeUtils: TimeUtils,
    private val resolutionListFactory: ResolutionListFactory,
    private val visualizationUtils: VisualizationUtils
) : BaseRoutingViewModel<EditArtViewState, EditArtViewEvent, MainDestination>() {

    companion object {
        private const val CUSTOM_SIZE_MINIMUM_PX = 100
        private const val CUSTOM_SIZE_MAXIMUM_PX = 12000
        private const val DEFAULT_SELECTION = true
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
        private const val INITIAL_TYPE_CUSTOM_TEXT = ""
        private const val PREVIEW_BITMAP_MAX_SIZE_WIDTH_PX = 2000
        private const val PREVIEW_BITMAP_MAX_SIZE_HEIGHT_PX = 2000
        private const val CUSTOM_TEXT_MAXIMUM_LENGTH = 30
    }

    private lateinit var activities: List<Activity>
    private val activitiesFiltered: List<Activity> get() = activitiesFilteredByFilterType[EditArtFilterType.filterFinal]!!
    private lateinit var activitiesFilteredByFilterType: MutableMap<EditArtFilterType, List<Activity>>
    private val activitiesFilteredSumDistance: Double get() = activitiesFiltered.sumOf { it.distance }
    private var activitiesTypesSelectionsMap: Map<String, Boolean> = mapOf()
    private lateinit var activitiesDistancesList: List<Double>
    private var activitiesDateSelections: List<DateSelection>? = null

    /** Determine current start and end MS of selected Date filters  **/
    private val activitiesDateRangeUnixMs: LongProgression?
        get() = activitiesDateSelections?.let { selections ->
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
                    TYPE -> activitiesTypesSelectionsMap[activity.type] ?: true
                    DISTANCE -> filterDistanceSelected?.let {
                        activity.distance in it
                    } ?: true
                }
            }
            activitiesFilteredByFilterType[this@updateFilteredActivities] = filteredActivities
        }
    }

    /** Sets [activitiesTypesSelectionsMap], [activitiesUnixMsList]: the filters which the
     * user may control. Must not be invoked before [activities] is populated. **/
    private fun EditArtFilterType.updateFilters() {
        val filteredActivities = (activitiesFilteredByFilterType[lastFilter] ?: activities)
        when (this) {
            DATE -> {
                filteredActivities
                    .map { SECONDS.toMillis(timeUtils.iso8601StringToUnixSecond(it.iso8601LocalDate)) }
                    .asRangeOrNullIfEmpty()
                    ?.let {
                        activitiesDateSelections = mutableListOf<DateSelection>(DateSelection.All)
                            .apply {
                                addAll(filteredActivities
                                    .map { timeUtils.iso8601StringToYearMonth(it.iso8601LocalDate).first }
                                    .distinct()
                                    .map { DateSelection.Year(it) }
                                    .toMutableList<DateSelection>()
                                    .apply {
                                        add(
                                            DateSelection.Custom(
                                                dateSelected = null,
                                                dateTotal = it
                                            )
                                        )
                                    })
                            }
                    }
            }
            TYPE -> activitiesTypesSelectionsMap = filteredActivities
                .distinctlyMapAndAssociateWith(Activity::type, activitiesTypesSelectionsMap)
            DISTANCE -> activitiesDistancesList = filteredActivities
                .map { it.distance }
        }
    }

    /** Extension Functions **/
    private fun List<Activity>.distinctlyMapAndAssociateWith(
        property: KProperty1<Activity, String>,
        previousMap: Map<String, Boolean>
    ): Map<String, Boolean> {
        return distinctBy(property)
            .map(property)
            .associateWith { previousMap[it] ?: DEFAULT_SELECTION }
    }

    private fun List<Long>.asRangeOrNullIfEmpty(): LongProgression? {
        return maxOrNull()?.let { max -> minOrNull()?.rangeTo(max) }
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

    /** Pushes updated filter information as set in [updateFilters] to the View **/
    private fun EditArtFilterType.pushUpdatedFiltersToView() {
        copyLastState {
            when (this@pushUpdatedFiltersToView) {
                DATE -> {
                    copy(
                        filterDateSelections = activitiesDateSelections
                        // dont really know what this was doing, investigate
                        /*
                filterDateYearsList = activitiesYears,
                filterDateSelected = filterDateSelected?.run {
                    last
                        .takeIf { it >= (newRange?.first ?: Long.MAX_VALUE) }
                        ?.let { adjEnd ->
                            first
                                .takeIf { it <= (newRange?.last ?: Long.MIN_VALUE) }
                                ?.rangeTo(adjEnd)
                        }
                },
                filterDateTotal = newRange

                         */
                    )
                }
                TYPE -> copy(filterTypes = activitiesTypesSelectionsMap.toList())
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
                filterActivitiesCountDate = DATE.activitiesCount,
                filterActivitiesCountDistance = DISTANCE.activitiesCount,
                filterActivitiesCountType = TYPE.activitiesCount,
                filterDateSelections = activitiesDateSelections,
                filterDateSelectionIndex = activitiesDateSelections?.indexOfFirst { it is DateSelection.All }
                    ?: -1,
                filterDistanceSelected = null,
                filterDistanceTotal = activitiesDistancesList
                    .takeIf { it.isNotEmpty() }
                    ?.run { min()..max() }, // todo rewrite this with the asrange ext
                filterTypes = activitiesTypesSelectionsMap.toList(),
                pagerStateWrapper = pagerStateWrapper,
                scrollStateFilter = ScrollState(INITIAL_SCROLL_STATE),
                scrollStateResize = ScrollState(INITIAL_SCROLL_STATE),
                scrollStateStyle = ScrollState(INITIAL_SCROLL_STATE),
                scrollStateType = ScrollState(INITIAL_SCROLL_STATE),
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
                styleFont = null,
                styleStrokeWidthType = MEDIUM,
                typeActivitiesDistanceMetersSummed = activitiesFiltered
                    .sumOf { it.distance }
                    .roundToInt(),
                typeAthleteName = athleteFromLocalUseCase(activities.first().athleteId)!!.fullName,
                typeFontSelected = FontType.QUICKSAND,
                typeFontWeightSelected = FontWeightType.REGULAR,
                typeFontItalicized = false,
                typeFontSizeSelected = FontSizeType.MEDIUM,
                typeMaximumCustomTextLength = CUSTOM_TEXT_MAXIMUM_LENGTH,
                typeLeftSelected = NONE,
                typeLeftCustomText = INITIAL_TYPE_CUSTOM_TEXT,
                typeCenterSelected = NONE,
                typeCenterCustomText = INITIAL_TYPE_CUSTOM_TEXT,
                typeRightSelected = NONE,
                typeRightCustomText = INITIAL_TYPE_CUSTOM_TEXT
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
                it.pushUpdatedFiltersToView()
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
            val replacementCustom = (activitiesDateSelections
                ?.get(filterDateSelectionIndex) as? DateSelection.Custom
                ?: error("ApiError retrieving DateSelection as Custom from selected index."))
                .run {
                    val selectedEnd = dateSelected?.last ?: dateTotal.last
                    val selectedStart = dateSelected?.first ?: dateTotal.first
                    val totalEnd = dateTotal.last

                    copy(
                        dateSelected = when (event) {
                            is FilterChanged.FilterDateCustomChanged.FilterAfterChanged -> {
                                event.changedTo.coerceAtMost(totalEnd).rangeTo(selectedEnd)
                            }
                            is FilterChanged.FilterDateCustomChanged.FilterBeforeChanged -> {
                                selectedStart.rangeTo(event.changedTo.coerceAtMost(totalEnd))
                            }
                        }
                    )
                }

            activitiesDateSelections = activitiesDateSelections
                ?.toMutableList()
                ?.apply {
                    set(filterDateSelectionIndex, replacementCustom)
                }

            copy(filterDateSelections = activitiesDateSelections).push()
        }
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
            copy(filterTypes = activitiesTypesSelectionsMap.toList())
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
                val filterRange = activitiesDateRangeUnixMs
                routeTo(
                    NavigateSaveArt(
                        activityTypes = filterTypes
                            .filter { it.second }
                            .map { it.first },
                        colorActivitiesArgb = styleActivities.color.toArgb(),
                        colorBackgroundArgb = styleBackground.color.toArgb(),
                        colorFontArgb = (styleFont ?: styleActivities).color.toArgb(),
                        filterAfterMs = filterRange?.first ?: Long.MIN_VALUE,
                        filterBeforeMs = filterRange?.last ?: Long.MAX_VALUE,
                        filterDistanceLessThan = filterDistanceSelected?.endInclusive
                            ?: Double.MAX_VALUE,
                        filterDistanceMoreThan = filterDistanceSelected?.start ?: Double.MIN_VALUE,
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
                    FONT -> copy(styleFont = (styleFont ?: styleActivities).copyWithEvent(event))
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
        val newText: String? = event.changedTo.takeIf { it.length <= CUSTOM_TEXT_MAXIMUM_LENGTH }
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
                    activities = activitiesFilteredByFilterType[EditArtFilterType.filterFinal]!!,
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
                    NAME -> typeAthleteName
                    DISTANCE_MILES -> activitiesFilteredSumDistance.meterToMilesStr()
                    DISTANCE_KILOMETERS -> activitiesFilteredSumDistance.meterToKilometerStr()
                    CUSTOM -> typeCustomText.second.takeIf { it.isNotBlank() }
                }
            }
        }

    private fun Double.meterToMilesStr(): String = "${(this * 0.000621371192).roundToInt()} mi"
    private fun Double.meterToKilometerStr(): String = "${(this / 1000f).roundToInt()} km"
}