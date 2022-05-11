package com.company.athleteapiart.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.presentation.ui.theme.Lato
import com.company.athleteapiart.presentation.ui.theme.StravaOrange

class Table {

    companion object {

        @Composable
        fun TableComposable(
            modifier: Modifier,
            columns: Array<String>, // YEAR, MONTH, ACTIVITY COUNT
            rows: List<Map<String, Pair<String, Boolean>>>, // YEAR to 2002, TRUE (true == bold)
            onSelectIndex: (Int) -> Unit,
            defaultSelected: Boolean = false
        ) {

            val scrollState = rememberScrollState()
            val scrollValue by remember { derivedStateOf { scrollState.value } }
            val scrollMax = scrollState.maxValue

            val selectedList = remember { mutableStateListOf<Boolean>() }

            for (i in 0..rows.lastIndex)
                selectedList.add(defaultSelected)

            BoxWithConstraints(modifier = modifier) {

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
                        for (column in columns)
                            Text(
                                text = column.uppercase(),
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                color = Color.Gray
                            )
                        // If we should show scrollbar, add a spacer for it of size of scrollbar
                        if (shouldShowScroll(scrollMax))
                            Spacer(
                                modifier = Modifier.width(scrollWidth(tableWidth).dp)
                            )
                    }
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .drawBehind {
                                    val canvasWidth = this.size.width
                                    val canvasHeight = this.size.height

                                    if (shouldShowScroll(maxValue = scrollMax)) {
                                        val scrollWidth = scrollWidth(tableWidth = tableWidth)

                                        drawRect(
                                            color = Color.LightGray,
                                            topLeft = scrollBackgroundPosition(
                                                tableWidth = tableWidth
                                            ),
                                            size = scrollBackgroundSize(
                                                canvasHeight = canvasHeight,
                                                scrollWidth = scrollWidth
                                            )
                                        )
                                        drawRect(
                                            color = StravaOrange,
                                            topLeft = scrollPosition(
                                                tableWidth = canvasWidth,
                                                canvasHeight = canvasHeight,
                                                value = scrollValue,
                                                maxValue = scrollMax
                                            ),
                                            size = scrollSize(
                                                scrollWidth = scrollWidth,
                                                maxValue = scrollMax,
                                                canvasHeight = canvasHeight
                                            )
                                        )
                                    }
                                }
                        ) {

                            for (i in 0..rows.lastIndex) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = selectedList[i],
                                        modifier = Modifier.weight(0.25f),
                                        onCheckedChange = {
                                            selectedList[i] = it
                                            onSelectIndex(i)
                                        })
                                    for (column in columns) {
                                        val field = rows[i][column]
                                        Text(
                                            text = field?.first ?: "ERR",
                                            fontSize = 24.sp,
                                            fontFamily = Lato,
                                            fontWeight = if (field?.second == true)
                                                FontWeight.Bold else FontWeight.Normal,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    if (shouldShowScroll(scrollMax))
                                        Spacer(
                                            modifier = Modifier
                                                .width(scrollWidth(tableWidth).dp)
                                        )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Invoked in Composable to help build ScrollBar with Canvas
        private fun shouldShowScroll(maxValue: Int) = maxValue != 0
        private fun scrollWidth(tableWidth: Float) = tableWidth * 0.03f
        private fun scrollPosition(
            tableWidth: Float,
            canvasHeight: Float,
            value: Int,
            maxValue: Int
        ) = Offset(
            x = tableWidth - scrollWidth(tableWidth),
            y = 0f + ((value.toFloat() / maxValue.toFloat()) * (canvasHeight - scrollSize(
                canvasHeight = canvasHeight,
                scrollWidth = scrollWidth(tableWidth),
                maxValue = maxValue
            ).height))
        )

        private fun scrollBackgroundPosition(tableWidth: Float) = Offset(
            x = tableWidth - scrollWidth(tableWidth),
            y = 0f
        )

        private fun scrollBackgroundSize(canvasHeight: Float, scrollWidth: Float) = Size(
            width = scrollWidth,
            height = canvasHeight
        )

        private fun scrollSize(canvasHeight: Float, scrollWidth: Float, maxValue: Int) = Size(
            width = scrollWidth,
            height = ((1f / maxValue) * 30f) * canvasHeight
        )

    }
}