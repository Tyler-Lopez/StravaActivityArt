package com.company.athleteapiart.presentation.athletescreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.athleteapiart.data.remote.responses.Activity
import com.company.athleteapiart.util.AthleteActivities
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import decodePoly

//import com.google.maps.android.PolyUtil

@Composable
fun AthleteScreen(
    viewModel: AthleteViewModel = hiltViewModel()
) {
    val activities by remember { AthleteActivities.activities }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    Column() {
        Button(onClick = { if (activities.isEmpty()) viewModel.getAccessToken() }) {
            Text("click")
        }
        activities.forEach {
            ActivityDrawing(it)
        }
    }

}


@Composable
fun ActivityDrawing(
    activity: Activity,
) {
    /*
    val coordinateList = activity.map.summary_polyline.decodePoly()

    var leftBound: Double = Double.MAX_VALUE
    var topBound: Double = Double.MAX_VALUE
    var bottomBound: Double = Double.MIN_VALUE
    var rightBound: Double = Double.MIN_VALUE

    for (point in coordinateList) {
        if (point.longitude < leftBound)
            leftBound = point.longitude
        if (point.longitude > rightBound)
            rightBound = point.longitude
        if (point.latitude < topBound)
            topBound = point.latitude
        if (point.latitude > bottomBound)
            bottomBound = point.latitude
    }

    val height = bottomBound - topBound
    val width = rightBound - leftBound
    val centerY = topBound + height / 2.0
    val centerX = leftBound + width / 2.0

    val canvasHeight = 300.dp
    val canvasWidth = 300.dp
    */

    Text(activity.name)

    // Image(painter = painterResource(id = activity.map.id), contentDescription = "")

}
