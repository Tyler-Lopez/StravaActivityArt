package com.company.athleteapiart.presentation.visualize_screen

import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.presentation.common.ButtonComposable
import com.company.athleteapiart.presentation.common.HeaderComposable
import com.company.athleteapiart.presentation.ui.theme.Asphalt
import com.company.athleteapiart.presentation.ui.theme.Coal
import com.company.athleteapiart.presentation.ui.theme.Icicle
import com.company.athleteapiart.presentation.ui.theme.StravaOrange
import com.company.athleteapiart.presentation.visualize_screen.VisualizeScreenState.*

@Composable
fun VisualizeScreen(
    athleteId: Long,
    yearMonths: Array<Pair<Int, Int>>,
    navController: NavHostController,
    activityTypes: Array<String>? = null, // If null then do not filter by activityTypes
    gears: Array<String?>? = null, // If null do not filter, if string is null then that means null gearId is included
    distances: ClosedFloatingPointRange<Float>? = null,
    viewModel: VisualizeScreenViewModel = hiltViewModel()
) {

    val screenState by remember { viewModel.screenState }
    val context = LocalContext.current
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Icicle),
        contentAlignment = Alignment.Center
    ) {

        val maxWidth = this.maxWidth
        val maxHeight = this.maxHeight

        when (screenState) {
            LAUNCH -> {
                println("launch invoked")
                viewModel.loadActivities(
                    context = context,
                    athleteId = athleteId,
                    yearMonths = yearMonths,
                    activityTypes = activityTypes,
                    gears = gears,
                    distances = distances
                )
            }
            GET_SPECIFICATION -> {
                val width = LocalDensity.current.run { maxWidth.roundToPx() }
                val backgroundPaint = Paint().also { it.color = Coal.toArgb() }
                val activityPaint = Paint().also { it.color = StravaOrange.toArgb() }

                viewModel.loadVisualizeSpecification(
                    bitmapWidth = width,
                    widthHeightRatio = 1080f / 1920f,
                    marginFraction = 0.05f,
                    backgroundPaint = backgroundPaint,
                    activityPaint = activityPaint
                )
            }
            LOADING -> {
                Text("Loading")
            }
            PERMISSION_ACCEPTED -> {

            }
            STANDBY -> {

                Column(
                    modifier = Modifier.widthIn(360.dp, maxWidth * 0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        elevation = 4.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .heightIn(0.dp, maxHeight * 0.75f)
                    ) {
                        VisualizeImage(
                            bitmap = visualizeBitmapMaker(
                                viewModel.visualizationSpec!!
                            )
                        )
                    }
                    ButtonComposable(
                        text = "Save Image",
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.Save
                    ) {

                    }
                }


                /*

                val backgroundPaint = Paint().also {
                    it.color = android.graphics.Color.WHITE
                }
                val activityPaint = Paint().also {
                    it.color = android.graphics.Color.parseColor("#fc4c02")
                }
                Card(elevation = 4.dp) {
                    VisualizeImage(
                        bitmap = visualizeBitmap(
                            bitmapWidth = LocalDensity.current.run { maxWidth.roundToPx() },
                            heightWidthRatio = 1920f / 1080f,
                            activities = viewModel.activities,
                            backgroundPaint = backgroundPaint,
                            activityPaint = activityPaint
                        )
                    )
                }

                 */
            }
        }
    }
}