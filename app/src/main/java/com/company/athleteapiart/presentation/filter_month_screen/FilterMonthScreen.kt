package com.company.athleteapiart.presentation.filter_month_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.presentation.filter_month_screen.FilterMonthScreenState.*
import com.company.athleteapiart.presentation.ui.theme.Lato

@Composable
fun FilterMonthScreen(
    athleteId: Long,
    years: Array<Int>,
    navController: NavHostController,
    viewModel: FilterMonthViewModel = hiltViewModel()
) {

    val screenState by remember { viewModel.filterMonthScreenState }
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val maxHeight = this.maxHeight

        when (screenState) {
            LAUNCH -> SideEffect {
                viewModel.loadActivities(
                    context = context,
                    athleteId = athleteId,
                    years = years
                )
            }
            LOADING -> {
                Text("Loading")
            }
            STANDBY -> {
                val yearMonthsData = viewModel.yearMonthsData
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(360.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Which months would you like to include?",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = Lato,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(maxHeight * 0.5f)
                            .verticalScroll(scrollState)
                            .drawBehind {
                                val width = size.width
                                val height = size.height
                                drawRect(
                                    color = Color.LightGray,
                                    topLeft = Offset(width - (width * 0.02f), 0f),
                                    size = Size(width * 0.02f, height)
                                )
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.LightGray)
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckBox,
                                tint = Color.Gray,
                                modifier = Modifier.weight(0.25f),
                                contentDescription = ""
                            )
                            Text(
                                text = "MONTH",
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                color = Color.Gray
                            )
                            Text(
                                text = "YEAR",
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                color = Color.Gray
                            )
                            Text(
                                text = "NO. ACTIVITIES",
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                color = Color.Gray
                            )
                        }
                        for (year in years)
                            for (month in 1..12) {
                                yearMonthsData[Pair(year, month)]?.also {
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Checkbox(
                                            checked = it.second,
                                            modifier = Modifier.weight(0.25f),
                                            onCheckedChange = {
                                                //  activities[i] = Triple(
                                                //      year.first, year.second, !year.third
                                                //  )
                                            })
                                        // MONTH
                                        Text(
                                            text = "$month",
                                            fontSize = 24.sp,
                                            fontFamily = Lato,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.weight(1f)
                                        )
                                        // YEAR
                                        Text(
                                            text = "$year",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = Lato,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.weight(1f)
                                        )

                                        // ACTIVITY COUNT IN YEAR-MONTH
                                        Text(
                                            text = "${it.first}",
                                            fontSize = 24.sp,
                                            fontFamily = Lato,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                    }
                    Button(
                        onClick = {}, modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text("Continue")
                    }
                }
            }
        }
    }
}