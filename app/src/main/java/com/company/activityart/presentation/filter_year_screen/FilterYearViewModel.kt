package com.company.activityart.presentation.filter_year_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.company.activityart.architecture.BaseRoutingViewModel
import com.company.activityart.architecture.Router
import com.company.activityart.domain.models.Athlete
import com.company.activityart.domain.use_case.athlete.GetAthleteUseCase
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.MainDestination.NavigateLogin
import com.company.activityart.presentation.MainDestination.NavigateUp
import com.company.activityart.presentation.filter_year_screen.FilterYearViewEvent.ContinueClicked
import com.company.activityart.presentation.filter_year_screen.FilterYearViewEvent.NavigateUpClicked
import com.company.activityart.presentation.filter_year_screen.FilterYearViewState.Loading
import com.company.activityart.presentation.filter_year_screen.FilterYearViewState.Standby
import com.company.activityart.util.NavArg
import com.company.activityart.util.ext.accessToken
import com.company.activityart.util.ext.athleteId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterYearViewModel @Inject constructor(
    private val getAthleteUseCase: GetAthleteUseCase,
    savedStateHandle: SavedStateHandle
) : BaseRoutingViewModel<FilterYearViewState, FilterYearViewEvent, MainDestination>() {


    private val athleteId: Long = savedStateHandle.athleteId
    private val accessToken: String = savedStateHandle.accessToken

    init {
        pushState(Loading)
        println(athleteId)
        println(accessToken)
    }

    override fun onEvent(event: FilterYearViewEvent) {
        viewModelScope.launch {
            when (event) {
                is ContinueClicked -> onContinueClicked()
                is NavigateUpClicked -> onNavigateUpClicked()
            }
        }
    }

    private fun onContinueClicked() {

    }

    private fun onNavigateUpClicked() {
        routeTo(NavigateUp)
    }

    override fun onRouterAttached() {
        viewModelScope.launch {
            loadActivities()
        }
    }

    private fun loadActivities() {
    }

    /*
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
    private val _rows = mutableStateListOf<List<Pair<String, Boolean>>>()
    private val columnYear = "YEAR"
    private val columnNoActivities = "#"
    val rows: List<List<Pair<String, Boolean>>> = _rows
    val columns = arrayOf(columnYear, columnNoActivities)

    // Screen ViewState
    private val _timeSelectScreenState = mutableStateOf(LAUNCH)
    val timeSelectScreenState: State<TimeSelectScreenState> = _timeSelectScreenState

    // StringConstants
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
                for (year in StringConstants.FIRST_YEAR..currentYear) {
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
                        // If lastCachedMonth != 12, we must cache
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
                                    // This is messy for now but need to check to ensure no valid activities
                                    if (response.data.any {
                                            it.activityYear == year && it.summaryPolyline != null
                                        }) {
                                        yearlyActivities.addAll(response.data)
                                        _selectedActivities.add(defaultSelected)
                                        _rows.add(
                                            listOf(
                                                "$year" to true,
                                                "${
                                                    yearlyActivities.filter {
                                                        it.activityYear == year && it.summaryPolyline != null
                                                    }.size
                                                }" to false
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
                                    } else year to 12 // This was changed from -1... we were calling for every single year and shouldn't have been
                                }
                                is Failure -> {
                                    year to -1
                                }
                            }
                            // Otherwise if it is already cached
                        } else {
                            if (yearlyActivities.isNotEmpty()) {
                                _selectedActivities.add(defaultSelected)
                                _rows.add(
                                    listOf(
                                        "$year" to true,
                                        "${
                                            yearlyActivities.filter {
                                                it.activityYear == year && it.summaryPolyline != null
                                            }.size
                                        }" to false
                                    )
                                )
                            }
                            // Return value of -1 so that it is filtered in the next logic check
                            // This is not very great and messy so must refactor
                            year to -1
                        }
                    }
                    caches.add(cache)
                }
                setAthleteUseCases.setAthlete(
                    context = context,
                    athleteEntity = athlete.withNewCaches(
                        caches.awaitAll().toMap().filter { it.value > 0 })
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
            val value = _rows[index][1].first.toInt()
            _selectedActivitiesCount.value =
                _selectedActivitiesCount.value + (value * if (selectedActivities[index]) 1 else -1)
        }
    }

    fun selectedYearsNavArgs(): String =
        buildString {
            _rows.forEachIndexed { index, fields ->
                if (selectedActivities[index])
                    append(fields[0].first).append(StringConstants.NAV_DELIMITER)
            }
        }


 */
}
