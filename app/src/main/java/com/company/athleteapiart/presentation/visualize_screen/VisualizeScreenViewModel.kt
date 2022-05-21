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
        bitmapWidth: Int, // e.g. 400
        widthHeightRatio: Float, // e.g. 1920f / 1080f
        marginFraction: Float, // e.g. .05 = 5% margin
        backgroundPaint: Paint,
        activityPaint: Paint,
    ) {

        _screenState.value = LOADING

        viewModelScope.launch(Dispatchers.Default) {

            // Determine height of bitmap given width and ratio
            val bitmapHeight = (bitmapWidth / widthHeightRatio).toInt()

            // Define how large the activities portion of bitmap should be
            val activitiesWidth = bitmapWidth * (1f - (marginFraction * 2f))
            val activitiesHeight = bitmapHeight * (1f - (marginFraction * 2f))

            val n = activities.size

            // https://math.stackexchange.com/questions/466198/algorithm-to-get-the-maximum-size-of-n-squares-that-fit-into-a-rectangle-with-a
            // PART 1: Computing theoreticals
            // E.g. 1920 / 1080 == 1.78
            // 50 activities * 1.778 = 88.9
            // Theoretical Columns = Sqrt() = 9.428
            // Theoretical Rows = 50 activities / 9.428 = 5.3
            // NOTE: An activity cannot be represented by .428 or .3
            // Therefore, we need to figure out whether to have 6 rows or 10 columns...
            val theoCols = sqrt(n * widthHeightRatio)
            val theoRows = n / theoCols

            // PART 2: Compute from ceiling of theoretical maximum rows (from height)
            var rowsHeight = ceil(theoRows) // 5.3 --> 6
            var colsHeight = ceil(n / rowsHeight) // 50 / 6 = 8.3 --> 9
            while (rowsHeight * widthHeightRatio < colsHeight)
                colsHeight = ceil(n / ++rowsHeight)
            val cellHeight = activitiesHeight / rowsHeight

            // PART 3: Computer from ceiling of theoretical maximum columns (from width)
            var colsWidth = ceil(theoCols)
            var rowsWidth = ceil(n / colsWidth)
            while (colsWidth < rowsWidth * widthHeightRatio)
                rowsWidth = ceil(n / ++colsWidth)
            val cellWidth = activitiesWidth / colsWidth

            var rowCount = 0f
            var colCount = 0f
            var activitySize = 0f

            if (cellWidth > cellHeight) {
                rowCount = rowsWidth
                colCount = colsWidth
                activitySize = cellWidth
            } else {
                rowCount = rowsHeight
                colCount = colsHeight
                activitySize = cellHeight
            }


            val initialOffset = Offset(
                x = (activitiesWidth - (activitySize * colCount)) / 2f,
                y = (activitiesHeight - (activitySize * rowCount)) / 2f
            )

            // This determines if we are on a "remainder row" i.e. 3rd row with 14 activities and 5 columns
            // and sets centerOffset equal to the amount we would need to nudge all activities on that row
            val lastRowOffset = ((rowCount * colCount) - n).let { missingCells ->
                (missingCells * activitySize) / 2f
            }

            val activityPaths = _activities.mapIndexed { index, act ->
                // Decode each Polyline into a List<LatLng>
                PolyUtil.decode(act.summaryPolyline).let { latLngList ->

                    // Convert List<LatLng> to List<Pair<Float, Float>>
                    val xOffset =
                        initialOffset.x + ((index % colCount) * activitySize) +
                                (bitmapWidth * marginFraction) +
                                if (ceil((index + 1) / colCount) >= rowCount) lastRowOffset
                                else 0f
                    val yOffset =
                        initialOffset.y + ((floor(index / colCount) % rowCount) * activitySize) +
                                (bitmapHeight * marginFraction)

                    val left = latLngList.minOf { it.longitude }
                    val right = latLngList.maxOf { it.longitude }
                    val top = latLngList.maxOf { it.latitude }
                    val bottom = latLngList.minOf { it.latitude }

                    val largestSide = maxOf(top - bottom, right - left)
                    val multiplier = (activitySize * 0.8f) / largestSide

                    latLngList.map { latLng ->
                        Pair(
                            first = (((latLng.longitude - ((
                                    left + right
                                    ) / 2f)) * multiplier) + xOffset + (activitySize / 2f)).toFloat(),
                            second = (((latLng.latitude - ((
                                    top + bottom
                                    ) / 2f)) * -1f * multiplier) + yOffset + (activitySize / 2f)).toFloat()
                        )
                    }

                    // Reduce List<LatLng> to Path
                }.let { floatList ->
                    Path().also { path ->
                        floatList.forEachIndexed { fIndex, pair ->
                            if (fIndex == 0)
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
                activityPaint = activityPaint.also {
                    it.isAntiAlias = true
                    it.strokeCap = Paint.Cap.ROUND
                    it.style = Paint.Style.STROKE
                    it.strokeWidth = sqrt(activitySize) * 0.25f
                },
                activities = activityPaths
            )

            _screenState.value = STANDBY
        }
    }
}