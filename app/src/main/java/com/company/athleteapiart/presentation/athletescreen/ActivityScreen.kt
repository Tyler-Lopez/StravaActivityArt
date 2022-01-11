package com.company.athleteapiart.presentation.athletescreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.athleteapiart.data.remote.responses.Activity
import com.company.athleteapiart.data.remote.responses.ActivityDetailed
import com.company.athleteapiart.util.AthleteActivities
import com.google.maps.android.PolyUtil

//import com.google.maps.android.PolyUtil

@Composable
fun ActivityScreen(
    viewModel: ActivityViewModel = hiltViewModel()
) {
    val activity by remember { viewModel.activity }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    Column(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Text("Loading activity...")
        } else {
            ActivityDrawing(activity!!)
        }
    }

}


@Composable
fun ActivityDrawing(
    activity: ActivityDetailed,
) {
    Text(activity.name)
    val latLngList = PolyUtil.decode(activity.map.summary_polyline)

    val lat = 43.60633
    val lon = -84.22043

    val points = mutableListOf<Offset>()

    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.Blue)) {
        for (i in 0..latLngList.lastIndex) {
            val point = Offset(
                ((latLngList[i].latitude.minus(lat)) * 200000.0).toFloat() + this.center.x,
                ((latLngList[i].longitude.minus(lon)) * 200000.0).toFloat() + this.center.y
            )
            points.add(point)
        }
        drawPoints(
            points = points,
            pointMode = PointMode.Polygon,
            color = Color.Magenta,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
        )
    }


    // Image(painter = painterResource(id = activity.map.id), contentDescription = "")

}
