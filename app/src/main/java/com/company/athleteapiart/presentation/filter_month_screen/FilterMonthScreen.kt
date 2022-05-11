package com.company.athleteapiart.presentation.filter_month_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.presentation.filter_month_screen.FilterMonthScreenState.*
import com.company.athleteapiart.presentation.ui.theme.Lato
import com.company.athleteapiart.presentation.ui.theme.StravaOrange
import com.company.athleteapiart.util.TimeUtils

@Composable
fun FilterMonthScreen(
    athleteId: Long,
    years: Array<Int>,
    navController: NavHostController,
    viewModel: FilterMonthViewModel = hiltViewModel()
) {

    val screenState by remember { viewModel.filterMonthScreenState }
    val scrollState = rememberScrollState()
    val scrollValue by remember { derivedStateOf { scrollState.value } }
    val scrollMax = scrollState.maxValue
    val context = LocalContext.current

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val boxHeight = this.maxHeight

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
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(boxHeight * 0.5f)
                    ) {
                        val maxWidth = this.maxWidth
                        val tableWidth: Float = with(LocalDensity.current) { maxWidth.toPx() }

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
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
                                if (viewModel.shouldShowScroll(scrollMax))
                                    Spacer(
                                        modifier = Modifier.width(
                                            viewModel.scrollWidth(
                                                tableWidth
                                            ).dp
                                        )
                                    )

                            }
                            BoxWithConstraints(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .verticalScroll(scrollState)
                                        .drawBehind {
                                            val canvasWidth = this.size.width
                                            val canvasHeight = this.size.height

                                            if (viewModel.shouldShowScroll(scrollMax)) {
                                                val scrollWidth =
                                                    viewModel.scrollWidth(tableWidth)

                                                drawRect(
                                                    color = Color.LightGray,
                                                    topLeft = viewModel.scrollBackgroundPosition(
                                                        tableWidth
                                                    ),
                                                    size = viewModel.scrollBackgroundSize(
                                                        canvasHeight = canvasHeight,
                                                        scrollWidth = scrollWidth
                                                    )
                                                )
                                                drawRect(
                                                    color = StravaOrange,
                                                    topLeft = viewModel.scrollPosition(
                                                        tableWidth = canvasWidth,
                                                        canvasHeight = canvasHeight,
                                                        value = scrollValue,
                                                        maxValue = scrollMax
                                                    ),
                                                    size = viewModel.scrollSize(
                                                        scrollWidth = scrollWidth,
                                                        maxValue = scrollMax,
                                                        canvasHeight = canvasHeight
                                                    )
                                                )
                                            }
                                        }
                                ) {
                                    for (year in years)
                                        for (month in 1..12) {
                                            yearMonthsData[Pair(year, month)]?.also {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
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
                                                        text =
                                                        TimeUtils.monthIntToString(month)
                                                            .substring(0, 3)
                                                            .uppercase(),
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
                                                    if (viewModel.shouldShowScroll(scrollMax))
                                                        Spacer(
                                                            modifier = Modifier
                                                                .width(
                                                                    viewModel.scrollWidth(
                                                                        tableWidth
                                                                    ).dp
                                                                )
                                                        )
                                                }
                                            }
                                        }
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
                    Text("Scroll state max is ${scrollState.maxValue} value is $scrollValue")
                }
            }
        }
    }
}