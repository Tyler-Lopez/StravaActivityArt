package com.company.athleteapiart.presentation.filter_distance_screen

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.domain.use_case.ActivitiesUseCases
import com.company.athleteapiart.util.ScreenState
import com.company.athleteapiart.util.ScreenState.*
import com.company.athleteapiart.util.meterToMiles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterDistanceViewModel @Inject constructor(
    activitiesUseCases: ActivitiesUseCases
) : ViewModel() {

    // Use cases
    private val getActivitiesUseCase = activitiesUseCases.getActivitiesUseCase

    // State - observed in the view
    private val _screenState = mutableStateOf(LAUNCH)
    val screenState: State<ScreenState> = _screenState

    // Distance minimums and maximums
    private val _distanceRange = mutableStateOf(Float.MIN_VALUE..Float.MAX_VALUE)
    private val _selectedRange = mutableStateOf(Float.MIN_VALUE..Float.MAX_VALUE)
    val distanceRange: State<ClosedFloatingPointRange<Float>> = _distanceRange
    val selectedRange: State<ClosedFloatingPointRange<Float>> = _selectedRange

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
        }
    }

    fun onSelectedChange(selectedRange: ClosedFloatingPointRange<Float>) {
        _selectedRange.value = selectedRange
        recalculateSelected()
    }

    private fun recalculateSelected() {
        _selectedCount.value = activityDistances.filter {
            it in _selectedRange.value
        }.size
    }
}