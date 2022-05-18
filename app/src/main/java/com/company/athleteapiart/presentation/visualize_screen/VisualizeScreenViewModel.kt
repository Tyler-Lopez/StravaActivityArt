package com.company.athleteapiart.presentation.visualize_screen

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.domain.use_case.ActivitiesUseCases
import com.company.athleteapiart.presentation.visualize_screen.VisualizeScreenState.*
import com.company.athleteapiart.util.meterToMiles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VisualizeScreenViewModel @Inject constructor(
    activitiesUseCases: ActivitiesUseCases
) : ViewModel() {
    // Use cases
    private val getActivitiesUseCase = activitiesUseCases.getActivitiesUseCase

    // Screen State
    private val _screenState = mutableStateOf(LAUNCH)
    val screenState: State<VisualizeScreenState> = _screenState

    // Activities
    private val _activities = mutableStateListOf<ActivityEntity>()
    val activities: List<ActivityEntity> = _activities

    fun loadActivities(
        context: Context,
        athleteId: Long,
        yearMonths: Array<Pair<Int, Int>>,
        activityTypes: Array<String>? = null, // If null then do not filter by activityTypes
        gears: Array<String?>? = null, // If null do not filter, if string is null then that means null gearId is included
        distances: ClosedFloatingPointRange<Float>? = null,
    ) {
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
            println("$distances are distances")
            // Apply filters and add to activities
            _activities.addAll(unsortedActivities.awaitAll()
                .flatten()
                .filter { act ->
                    activityTypes?.contains(act.activityType) ?: true &&
                            gears?.contains(act.gearId) ?: true &&
                            distances?.let { act.activityDistance.meterToMiles() in it} ?: true
                })

            _screenState.value = STANDBY
        }
    }
}