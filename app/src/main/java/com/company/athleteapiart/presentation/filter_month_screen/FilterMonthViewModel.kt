package com.company.athleteapiart.presentation.filter_month_screen

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.domain.use_case.ActivitiesUseCases
import com.company.athleteapiart.presentation.filter_month_screen.FilterMonthScreenState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterMonthViewModel @Inject constructor(
    activitiesUseCases: ActivitiesUseCases
) : ViewModel() {

    // Use cases
    private val getActivitiesUseCase = activitiesUseCases.getActivitiesUseCase

    // State - observed in the view
    val filterMonthScreenState = mutableStateOf(LAUNCH)

    // Data - referenced in view
    // (YEAR, MONTH) to (NO. ACTIVITIES, SELECTED)
    val yearMonthsData = mutableMapOf<Pair<Int, Int>, Pair<Int, Boolean>>()

    fun loadActivities(
        context: Context,
        athleteId: Long,
        years: Array<Int>
    ) {
        filterMonthScreenState.value = LOADING

        val unsortedActivities = mutableListOf<Deferred<List<ActivityEntity>>>()

        viewModelScope.launch {
            for (year in years) {
                val yearActivities = async {
                    getActivitiesUseCase.getActivitiesByYearFromCache(
                        context = context,
                        athleteId = athleteId,
                        year = year
                    )
                }
                unsortedActivities.add(yearActivities)
            }
            // Iterate through all awaited yearly activities
            for (yearlyActivities in unsortedActivities.awaitAll()) {
                // Iterate through all activities of a given year
                for (activity in yearlyActivities) {
                    // Populate yearsMonthData accordingly
                    val key = Pair(activity.activityYear, activity.activityMonth)
                    yearMonthsData[key] =
                        Pair((yearMonthsData[key]?.first ?: 0) + 1, true)
                }
            }

            filterMonthScreenState.value = STANDBY
        }
    }

    // Invoked in View to help build ScrollBar with Canvas
    fun shouldShowScroll(maxValue: Int) = maxValue != 0
    fun scrollWidth(tableWidth: Float) = tableWidth * 0.03f
    fun scrollPosition(
        tableWidth: Float,
        canvasHeight: Float,
        value: Int,
        maxValue: Int
    ) = Offset(
        x = tableWidth - scrollWidth(tableWidth),
        y = 0f + ((value.toFloat() / maxValue.toFloat()) * (canvasHeight - scrollSize(
            canvasHeight = canvasHeight,
            scrollWidth = scrollWidth(tableWidth),
            maxValue = maxValue
        ).height))
    )

    fun scrollBackgroundPosition(tableWidth: Float) = Offset(
        x = tableWidth - scrollWidth(tableWidth),
        y = 0f
    )

    fun scrollBackgroundSize(canvasHeight: Float, scrollWidth: Float) = Size(
        width = scrollWidth,
        height = canvasHeight
    )

    fun scrollSize(canvasHeight: Float, scrollWidth: Float, maxValue: Int) = Size(
        width = scrollWidth,
        height = ((1f / maxValue) * 30f) * canvasHeight
    )

}