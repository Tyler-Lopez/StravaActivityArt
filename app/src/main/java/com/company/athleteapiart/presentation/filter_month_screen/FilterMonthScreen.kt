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
import com.company.athleteapiart.presentation.common.Table
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
                //   val yearMonthsData = viewModel.yearMonthsData
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
                    Table.TableComposable(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(boxHeight * 0.5f),
                        columns = viewModel.getColumns(),
                        rows = viewModel.getRows(),
                        onSelectIndex = {

                        }
                    )
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