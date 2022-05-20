package com.company.athleteapiart.presentation.visualize_screen

import android.content.Context
import android.graphics.Paint
import android.graphics.Path
import android.provider.Settings
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.athleteapiart.data.entities.ActivityEntity
import com.company.athleteapiart.domain.use_case.ActivitiesUseCases
import com.company.athleteapiart.presentation.visualize_screen.VisualizeScreenState.*
import com.company.athleteapiart.util.meterToMiles
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sqrt

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

    // Visualization Specification
    var visualizationSpec: VisualizeSpecification? = null
        private set

    // Load activities from ROOM
    fun loadActivities(
        context: Context,
        athleteId: Long,
        yearMonths: Array<Pair<Int, Int>>,
        activityTypes: Array<String>? = null, // If null then do not filter by activityTypes
        gears: Array<String?>? = null, // If null do not filter, if string is null then that means null gearId is included
        distances: ClosedFloatingPointRange<Float>? = null,
    ) {
        _screenState.value = LOADING

        viewModelScope.launch(Dispatchers.Default) {
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
            // Apply filters and add to activities
            _activities.addAll(unsortedActivities.awaitAll()
                .flatten()
                .filter { act ->
                    activityTypes?.contains(act.activityType) ?: true &&
                            gears?.contains(act.gearId) ?: true &&
                            distances?.let { act.activityDistance.meterToMiles() in it } ?: true
                })
            _screenState.value = GET_SPECIFICATION
        }
    }


    // Convert activities into VisualizeSpecification
    fun loadVisualizeSpecification(
        bitmapWidth: Int,
        heightWidthRatio: Float,
        backgroundPaint: Paint,
        activityPaint: Paint,
    ) {
        println("load visualize specification invoked")

        _screenState.value = LOADING

        viewModelScope.launch(Dispatchers.Default) {

            // Determine height of image given width and ratio
            val bitmapHeight = (bitmapWidth * heightWidthRatio).toInt()

            val n = activities.size

            // https://math.stackexchange.com/questions/466198/algorithm-to-get-the-maximum-size-of-n-squares-that-fit-into-a-rectangle-with-a
            // Number of activities that can fit in columns and rows
            // Size of activity is eq. to height / small
            val activitySize = minOf(
                // Height / # Rows from attempting to fill whole width
                bitmapHeight / ceil(n / ceil(sqrt(n / heightWidthRatio)).toInt().toFloat()),
                // Width / # Cols from attempting to fill whole height
                bitmapWidth / ceil(n / ceil(sqrt(n * heightWidthRatio)).toInt().toFloat())
            )

            // Offsets the theoretical rectangle the activities occupy to be centered
            val initialOffset = Offset(
                x = bitmapWidth % activitySize,
                y = bitmapHeight % activitySize
            ) / 2f


            val activityPaths = _activities.mapIndexed { index, act ->
                // Decode each Polyline into a List<LatLng>
                PolyUtil.decode(act.summaryPolyline).let { latLngList ->
                    // Convert List<LatLng> to List<Pair<Float, Float>>
                    latLngList.map { latLng ->

                        val colCount = floor(bitmapWidth / activitySize)
                        val rowCount = floor(bitmapHeight / activitySize)

                        val xOffset = initialOffset.x + ((index % colCount) * activitySize)
                        val yOffset = initialOffset.y + ((index % rowCount) * activitySize)

                        Pair(
                            first = (latLng.latitude -
                                    ((latLngList.minOf { it.latitude } +
                                            latLngList.maxOf { it.latitude }) / 2f) +
                                    xOffset + (activitySize / 2f)).toFloat(),
                            second = ((latLng.longitude -
                                    ((latLngList.minOf { it.longitude } +
                                            latLngList.maxOf { it.longitude }) / 2f) * -1f) +
                                    yOffset + (activitySize / 2f)).toFloat()
                        )
                    }

                    // Reduce List<LatLng> to Path
                }.let { floatList ->
                    Path().also { path ->
                        floatList.forEachIndexed { index, pair ->
                            if (index == 0)
                                path.setLastPoint(pair.first, pair.second)
                            else
                                path.lineTo(pair.first, pair.second)
                        }
                    }
                }
            }

            visualizationSpec = VisualizeSpecification(
                visualizationWidth = bitmapWidth,
                visualizationHeight = bitmapHeight,
                backgroundPaint = backgroundPaint,
                activityPaint = activityPaint,
                activities = activityPaths
            )

            _screenState.value = STANDBY
        }
    }
}