package com.company.activityart.presentation.filter_distance_screen

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.activityart.data.entities.ActivityEntity
import com.company.activityart.domain.use_case.ActivitiesUseCases
import com.company.activityart.util.NavigationUtils
import com.company.activityart.util.ScreenState
import com.company.activityart.util.ScreenState.*
import com.company.activityart.util.meterToMiles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.floor

@HiltViewModel
class FilterDistanceViewModel @Inject constructor(
    activitiesUseCases: ActivitiesUseCases
) : ViewModel() {

    // Use cases
    private val getActivitiesUseCase = activitiesUseCases.getActivitiesUseCase

    // ViewState - observed in the view
    private val _screenState = mutableStateOf(LAUNCH)
    val screenState: State<ScreenState> = _screenState

    // Distance minimums and maximums
    private val _distanceRange = mutableStateOf(Float.MIN_VALUE..Float.MAX_VALUE)
    private val _selectedRange = mutableStateOf(Float.MIN_VALUE..Float.MAX_VALUE)
    val distanceRange: State<ClosedFloatingPointRange<Float>> = _distanceRange
    val selectedRange: State<ClosedFloatingPointRange<Float>> = _selectedRange

    private val _distanceRangeInt = mutableStateOf<IntRange?>(null)
    private val _distancesHeightMap = mutableStateOf<Map<Int, Float>?>(null)
    private val _selectedMiles = mutableStateOf<IntRange?>(null)
    val distanceRangeInt = _distanceRangeInt
    val distancesHeightMap = _distancesHeightMap
    val selectedMiles = _selectedMiles

    private fun fetchGraphInformation() {
        viewModelScope.launch {
            _distanceRangeInt.value =
                _distanceRange.value.start.toInt().._distanceRange.value.endInclusive.toInt()
            val toReturn = mutableMapOf<Int, Float>()
            val frequencyMap = mutableMapOf<Int, Int>()
            for (distance in activityDistances)
                frequencyMap[distance.toInt()] = (frequencyMap[distance.toInt()] ?: 0) + 1
            val maximum = frequencyMap.values.maxOrNull()
            for (distance in activityDistances)
                toReturn[distance.toInt()] = (frequencyMap[distance.toInt()]
                    ?: 0).toFloat() / (maximum ?: 1)
            _distancesHeightMap.value = toReturn
            setSelectedRange()
        }
    }

    private fun setSelectedRange() {
        println("Here selected is ${_selectedMiles.value}")
        _selectedMiles.value =
            floor(_selectedRange.value.start.toDouble()).toInt()..floor(_selectedRange.value.endInclusive.toDouble()).toInt()
        println("Here selected after is ${_selectedMiles.value}")
    }


    // Activities simplified down to their distance
    private val activityDistances = mutableStateListOf<Float>()
    private val _selectedCount = mutableStateOf(0)
    val selectedCount: State<Int> = _selectedCount


    fun loadActivities(
        context: Context,
        athleteId: Long,
        yearMonths: Array<Pair<Int, Int>>,
        activityTypes: Array<String>? = null,
        gears: Array<String?>? = null
    ) {
        // Set state to loading
        _screenState.value = LOADING

        viewModelScope.launch {
            // Make async calls to each month that we should load then await
            val unsortedActivities = mutableListOf<Deferred<List<ActivityEntity>>>()

            for (yearMonth in yearMonths) {
                unsortedActivities.add(async {
                    getActivitiesUseCase.getActivitiesByYearMonthFromCache(
                        context = context,
                        athleteId = athleteId,
                        year = yearMonth.first,
                        month = yearMonth.second
                    )
                })
            }
            // Iterate through each awaited activity and set minimum and maximum correctly
            // after filtering for activity type and gear as necessary
            unsortedActivities.awaitAll()
                .flatten()
                .filter {
                    activityTypes?.contains(it.activityType) ?: true &&
                            gears?.contains(it.gearId) ?: true
                }.forEach { activity ->
                    val distance = activity.activityDistance.meterToMiles().toFloat()
                    activityDistances.add(distance)
                }
            _distanceRange.value =
                (activityDistances.minOrNull() ?: 0f)..(activityDistances.maxOrNull() ?: 0f)
            _selectedRange.value = _distanceRange.value
            recalculateSelected()
            _screenState.value = STANDBY
            fetchGraphInformation()
        }
    }

    fun onSelectedChange(selectedRange: ClosedFloatingPointRange<Float>) {

        val previousRange =
            _selectedRange.value.start.toInt().._selectedRange.value.endInclusive.toInt()
        val newRange =
            selectedRange.start.toInt()..selectedRange.endInclusive.toInt()

        _selectedRange.value = selectedRange

        viewModelScope.launch {
            if (previousRange != newRange) {
                setSelectedRange()
            }
            recalculateSelected()
        }

    }

    private fun recalculateSelected() {
        _selectedCount.value = activityDistances.filter {
            it in _selectedRange.value
        }.size
    }

    // NAVIGATION ARGS
    fun distancesNavArgs() = NavigationUtils.distanceNavArgs(_selectedRange.value)
    fun activityTypesNavArgs(types: Array<String>?) = NavigationUtils.activityTypesNavArgs(types)
    fun gearsNavArgs(gears: Array<String?>?) = NavigationUtils.gearsNavArgs(gears)
    fun yearMonthsNavArgs(yearMonths: Array<Pair<Int, Int>>) =
        NavigationUtils.yearMonthsNavArgs(yearMonths)

}