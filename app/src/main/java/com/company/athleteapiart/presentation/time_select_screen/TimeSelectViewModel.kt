package com.company.athleteapiart.presentation.time_select_screen

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.domain.use_case.ActivitiesUseCases
import com.company.athleteapiart.domain.use_case.AthleteUseCases
import com.company.athleteapiart.util.Constants
import com.company.athleteapiart.util.Resource.*
import com.company.athleteapiart.presentation.time_select_screen.TimeSelectScreenState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TimeSelectViewModel @Inject constructor(
    athleteUseCases: AthleteUseCases,
    activitiesUseCases: ActivitiesUseCases
) : ViewModel() {

    // Use cases
    private val getActivitiesUseCase = activitiesUseCases.getActivitiesUseCase
    private val insertActivitiesUseCase = activitiesUseCases.insertActivitiesUseCase
    private val getAthleteUseCase = athleteUseCases.getAthleteUseCase
    private val setAthleteUseCases = athleteUseCases.setAthleteUseCase

    // Which activities are selected
    private val _selectedActivities = mutableStateListOf<Boolean>()
    private val _selectedActivitiesCount = mutableStateOf(0)
    val selectedActivities: List<Boolean> = _selectedActivities
    val selectedActivitiesCount: State<Int> = _selectedActivitiesCount

    // Rows & Columns
    private val _rows = mutableStateListOf<Map<String, String>>()
    private val columnYear = "YEAR"
    private val columnNoActivities = "NO. ACTIVITIES"
    val rows: List<Map<String, String>> = _rows
    val columns = arrayOf(Pair(columnYear, true), Pair(columnNoActivities, false))

    // Screen State
    private val _timeSelectScreenState = mutableStateOf(LAUNCH)
    val timeSelectScreenState: State<TimeSelectScreenState> = _timeSelectScreenState

    // Constants
    private val defaultSelected = false

    // Invoked upon LAUNCH
    fun loadActivities(
        context: Context,
        athleteId: Long,
        accessToken: String
    ) {
        // Set state to loading
        _timeSelectScreenState.value = LOADING

        viewModelScope.launch {

            // Clear loaded activities - probably would be improved if we just
            // added some logic to keep old data but specifically not repeat it
            _rows.clear()

            // Determine current year and current month
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

            // Get athlete - important for knowing IF activities have been cached
            val athlete = getAthleteUseCase.getAthlete(
                context,
                athleteId,
                accessToken
            ).data!!

            // Keep record of any changes to cache history
            // val newCaches = mutableMapOf<Int, Int>()
            launch {
                // Iterate through all years
                val caches = mutableSetOf<Deferred<Pair<Int, Int>>>()
                for (year in Constants.FIRST_YEAR..currentYear) {
                    val cache = async {
                        // Get last cached month of this year
                        val lastCachedMonth = athlete.lastCachedMonth(year)
                        val yearlyActivities = mutableListOf<ActivityEntity>()


                        // Add any cached activities
                        yearlyActivities.addAll(
                            getActivitiesUseCase
                                .getActivitiesByYearFromRoom(
                                    context = context,
                                    athleteId = athleteId,
                                    year = year,
                                    lastCachedMonth =
                                    // We currently NEVER pull from cached data into current month on current year
                                    lastCachedMonth - if (lastCachedMonth != 12) 1 else 0
                                )
                        )
                        println("Year: $year, LCM: $lastCachedMonth, Size: ${yearlyActivities.size}")
                        // Invoke API as needed
                        if (lastCachedMonth != 12) {
                            val response = getActivitiesUseCase
                                .getActivitiesAfterMonthInYearFromApi(
                                    accessToken = accessToken,
                                    year = year,
                                    afterMonth = lastCachedMonth - 1
                                )
                            println("After Response, size is: ${response.data?.size}")
                            when (response) {
                                is Success -> {
                                    yearlyActivities.addAll(response.data)
                                    _selectedActivities.add(defaultSelected)
                                    _rows.add(
                                        mapOf(
                                            columnYear to "$year",
                                            columnNoActivities to "${yearlyActivities.filter {
                                                it.activityYear == year && it.summaryPolyline != null
                                            }.size}"
                                        )
                                    )
                                    cacheActivities(
                                        context = context,
                                        activities = yearlyActivities.toTypedArray()
                                    )
                                    println("Here, year is $year, current month is $currentMonth")
                                    if (currentYear != year)
                                        year to 12
                                    else
                                        year to currentMonth
                                }
                                is Error -> {
                                    year to -1
                                }
                            }
                        }
                        else {
                            _selectedActivities.add(defaultSelected)
                            _rows.add(
                                mapOf(
                                    columnYear to "$year",
                                    columnNoActivities to "${yearlyActivities.filter {
                                        it.activityYear == year && it.summaryPolyline != null
                                    }.size}"
                                )
                            )
                            year to -1
                        }
                    }
                    caches.add(cache)
                }
                setAthleteUseCases.setAthlete(
                    context = context,
                    athleteEntity = athlete.withNewCaches(caches.awaitAll().toMap().filter { it.value > 0 })
                ).also {
                    _timeSelectScreenState.value = STANDBY
                }
            }
        }
    }

    // Update ROOM of ActivityDatabase and AthleteDatabase to cache and reflect cache
    private suspend fun cacheActivities(
        context: Context,
        activities: Array<ActivityEntity>
    ) {
        insertActivitiesUseCase.insertActivities(
            context = context,
            activities = activities
        )
    }

    // Invoked in view to say that the user has selected this index
    fun updateSelectedActivities(index: Int) {
        viewModelScope.launch {
            _selectedActivities[index] = !selectedActivities[index]
            val value = _rows[index][columnNoActivities]?.toInt() ?: 0
            _selectedActivitiesCount.value =
                _selectedActivitiesCount.value + (value * if (selectedActivities[index]) 1 else -1)
        }
    }

    fun selectedYearsNavArgs(): String =
        buildString {
            _rows.forEachIndexed { index, pair ->
                if (selectedActivities[index])
                    append(pair[columnYear]).append(Constants.NAV_YEAR_DELIMITER)
            }
        }

}
