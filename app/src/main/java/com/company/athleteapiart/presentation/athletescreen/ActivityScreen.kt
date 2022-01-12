package com.company.athleteapiart.presentation.athletescreen

import androidx.compose.runtime.getValue
import android.graphics.Bitmap
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.company.athleteapiart.data.remote.responses.ActivityDetailed
import com.company.athleteapiart.presentation.activity_visualize_screen.ActivityVisualizeView
import com.google.maps.android.PolyUtil

//import com.google.maps.android.PolyUtil

@Composable
fun ActivityScreen(
    viewModel: ActivityViewModel = hiltViewModel()
) {
    var activity by remember { viewModel.activity }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    Column(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Text("Loading activity...")
        } else {
            activity.forEach {
                ActivityDrawing(it, viewModel)
            }

        }
    }

}


@Composable
fun ActivityDrawing(
    activity: ActivityDetailed,
    viewModel: ActivityViewModel
) {
    Text(activity.name)
    AndroidView(
        factory = { context ->
            val activityVisualizeView = ActivityVisualizeView(ctx = context) {
                viewModel.bitmapCreated(it)
            }
            activityVisualizeView
        }
    )
    ActivityImageHandler(viewModel)
    val latLngList = PolyUtil.decode(activity.map.summary_polyline)

    val lat = latLngList[0].latitude
    val lon = latLngList[0].longitude

    val points = mutableListOf<Offset>()
/*
    Canvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.Blue)) {
        for (i in 0..latLngList.lastIndex) {
            val point = Offset(
                ((latLngList[i].latitude.minus(lat)) * 100000.0).toFloat() + this.center.x,
                ((latLngList[i].longitude.minus(lon)) * 100000.0).toFloat() + this.center.y
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

 */


    // Image(painter = painterResource(id = activity.map.id), contentDescription = "")

}
@Composable
fun ActivityImageHandler(viewModel: ActivityViewModel) {
    val bitmap = viewModel.onBitmapGenerated.observeAsState().value
    ActivityImage(bitmap = bitmap)
}

@Composable
fun ActivityImage(bitmap: Bitmap?) {
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}