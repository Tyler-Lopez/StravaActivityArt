package com.company.activityart.presentation.visualize_screen

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.*
import com.company.activityart.data.entities.ActivityEntity
import com.company.activityart.domain.use_case.ActivitiesUseCases
import com.company.activityart.presentation.ui.theme.Coal
import com.company.activityart.presentation.ui.theme.Pumpkin
import com.company.activityart.presentation.ui.theme.StravaOrange
import com.company.activityart.presentation.visualize_screen.VisualizeScreenState.*
import com.company.activityart.util.Constants
import com.company.activityart.util.meterToMiles
import com.company.activityart.util.saveImage
import com.google.maps.android.PolyUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

@HiltViewModel
class VisualizeScreenViewModel @Inject constructor(
    activitiesUseCases: ActivitiesUseCases
) : ViewModel() {
    // Use cases
    private val getActivitiesUseCase = activitiesUseCases.getActivitiesUseCase

    // Constant Factor to Shrink Size
    private val strokeWidthFactor = 0.0050f

    // Shrink stroke width relative to total activities
    private val activitiesCountStrokeFactor: Float
        get() = sqrt(sqrt(activityPaths.size.toFloat()))

    // Screen State
    private val _screenState = mutableStateOf(LAUNCH)
    val screenState: State<VisualizeScreenState> = _screenState

    // Activities
    private val _activities = mutableStateListOf<ActivityEntity>()
    val activities: List<ActivityEntity> = _activities

    // Permissions
    val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE

    // Paints
    private val backgroundPaint = Paint().also { it.color = Coal.toArgb() }
    private val activityPaint = Paint().also {
        it.color = StravaOrange.toArgb()
        it.style = Paint.Style.STROKE
        it.strokeJoin = Paint.Join.ROUND
        it.isAntiAlias = true
        // it.setShadowLayer(5f, 5f, 5f, Pumpkin.toArgb())
    }

    // Acceptable Resolutions
    val resolutions = ResolutionSpec.values().toList()
    private val _selectedResolution = mutableStateOf(1)
    val selectedResolution: State<Int> = _selectedResolution

    private val _isRotated = mutableStateOf(false)
    val isRotate: State<Boolean> = _isRotated

    fun rotate() {
        _isRotated.value = !_isRotated.value
    }

    fun updateResolution(index: Int) {
        _selectedResolution.value = index
        _screenState.value = GET_SPECIFICATION
    }


    // Margin size
    private val marginFraction = 0.05f // 5% margin

    // Bitmap State - referenced in view to create image composable
    private val _bitmapState = mutableStateOf<Bitmap?>(null)
    val bitmapState: State<Bitmap?> = _bitmapState

    // The following variables pertain specifically to the image composable
    private var visWidth: Int = 0
    private var visHeight: Int = 0
    private var activityPaths: List<Path> = listOf()

    // Updates the bitmap state specifically which is used in the Image composable
    private fun updateBitmapState(
        visWidth: Int,
        visHeight: Int
    ) {
        _bitmapState.value = visualizeBitmapMaker(
            VisualizeSpecification(
                visualizationWidth = visWidth,
                visualizationHeight = visHeight,
                backgroundPaint = backgroundPaint,
                activityPaint = activityPaint.also {
                    it.strokeWidth =
                        (sqrt(visWidth * visHeight.toFloat()) * strokeWidthFactor) / activitiesCountStrokeFactor
                },
                activities = activityPaths
            )
        )
    }

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
        composableWidth: Int, // e.g. 400
    ) {
        _screenState.value = LOADING

        viewModelScope.launch(Dispatchers.Default) {
            val composableHeight =
                (composableWidth / (resolutions[_selectedResolution.value].resolution.toList()
                    .reduce { x, y -> x / y })).toInt()

            visWidth = composableWidth
            visHeight = composableHeight
            activityPaths = computeActivityPaths(composableWidth)
            updateBitmapState(composableWidth, composableHeight)

            _screenState.value = STANDBY
        }
    }

    fun startSave(context: Context) {

        _screenState.value = SAVING

        viewModelScope.launch(Dispatchers.Default) {
            saveImage(
                bitmap = visualizeBitmapMaker(
                    VisualizeSpecification(
                        resolutions[_selectedResolution.value].resolution.first.toInt(),
                        resolutions[_selectedResolution.value].resolution.second.toInt(),
                        backgroundPaint,
                        activityPaint.also {
                            it.strokeWidth =
                                (resolutions[_selectedResolution.value].totalPixels * strokeWidthFactor) / activitiesCountStrokeFactor
                        },
                        computeActivityPaths(resolutions[_selectedResolution.value].resolution.first.toInt())
                    )
                ),
                context = context,
                folderName = Constants.IMAGE_DIRECTORY
            )
            _screenState.value = STANDBY
        }
    }

    private fun computeActivityPaths(
        bitmapWidth: Int, // e.g. 400
    ): List<Path> {
        val widthHeightRatio =
            resolutions[_selectedResolution.value].widthHeightRatio
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

        val rowCount: Float
        val colCount: Float
        val activitySize: Float

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

        return _activities.mapIndexed { index, act ->
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
    }
}