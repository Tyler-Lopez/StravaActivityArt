package com.company.athleteapiart.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.data.remote.responses.Activity
import com.company.athleteapiart.ui.theme.Roboto
import com.company.athleteapiart.ui.theme.RobotoCondensed
import com.company.athleteapiart.util.TimeUtils
import com.company.athleteapiart.util.meterToMiles

@Composable
fun ComposableActivityDetail(
    activity: Activity,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(0.8f)
        ) {
            Text(
                text = activity.name,
                fontSize = 25.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ComposableHeaderValue(
                    header = "Distance",
                    value = "%.2f".format(activity.distance.meterToMiles()) + " mi",
                    modifier = Modifier
                        .padding(end = 10.dp)
                )
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .height(25.dp)
                        .background(Color.LightGray)
                )

                ComposableHeaderValue(
                    header = "Pace",
                    value = "%.2f".format(30.0 / activity.average_speed) + " /mi",
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                )
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .height(25.dp)
                        .background(Color.LightGray)
                )
                ComposableHeaderValue(
                    header = "Time",
                    value = TimeUtils.timeToString(activity.elapsed_time),
                    modifier = Modifier
                        .padding(start = 10.dp)
                )
            }
        }
        // PICTURE THUMBNAIL WILL GO HERE
        Box(modifier = Modifier
            .weight(0.2f)
            .padding(5.dp)
            .height(100.dp)
            .background(Color.Blue))
    }
}

@Composable
fun ComposableHeaderValue(
    header: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = header,
            fontSize = 16.sp,
            fontFamily = RobotoCondensed,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 20.sp,
            fontFamily = RobotoCondensed,
            color = Color.DarkGray
        )

    }
}
