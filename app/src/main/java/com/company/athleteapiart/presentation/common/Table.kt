package com.company.athleteapiart.presentation.common

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenu
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
import com.company.athleteapiart.presentation.ui.theme.Asphalt
import com.company.athleteapiart.presentation.ui.theme.Lato
import com.company.athleteapiart.presentation.ui.theme.Pumpkin
import com.company.athleteapiart.presentation.ui.theme.StravaOrange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Table {


    companion object {

        data class TableSpecification(
            val tableWidth: Float,
            val scrollWidth: Float,
            val scrollHeight: Float,
            val showScroll: Boolean
        )

        data class ScrollSpecification(
            val scrollWidth: Int,
            val scrollHeight: Float,
            val scrollPosition: Offset
        )

        @Composable
        fun ScrollableRows(
            columns: Array<Pair<String, Boolean>>,
            rows: List<Map<String, String>>,
            scrollSpecification: ScrollSpecification? = null
        ) {
            rows.forEach { rowMap ->
                // Row representing DATA - SCROLLBAR
                Row(

                ) {
                    // Row representing DATA - DATA - DATA
                    Row {
                        columns.forEach { column ->
                            rowMap[column.first]
                        }
                    }
                    // If scrollSpec != null show scrollbar
                    scrollSpecification?.let { scrollSpec ->
                        // This represents scroll bar, add canvas draw behind here
                        Spacer(
                            modifier = Modifier
                                .width((scrollSpec.scrollWidth).dp)
                                .fillMaxHeight()
                                .background(Asphalt)
                                .drawBehind {
                                    drawRect(
                                        color = Pumpkin,
                                        topLeft = scrollSpec.scrollPosition,
                                        size = Size(
                                            // Convert scrollWidth from DP constant to a float
                                            width = LocalDensity.curre {scrollSpec.scrollWidth. },
                                            height = scrollSpec.scrollHeight
                                        )
                                    )
                                }
                        )
                    }
                }
            }
        }
    }


    @Composable
    fun TableComposable(
        modifier: Modifier,
        columns: Array<Pair<String, Boolean>>, // (YEAR, TRUE), (MONTH, TRUE), (ACTIVITY COUNT, FALSE)
        rows: List<Map<String, String>>, // YEAR to 2002
        tableSpecification: TableSpecification,
        selectionList: List<Boolean>,
        onSelectIndex: (Int) -> Unit
    ) {
        // Create ScrollState
        val scrollState = rememberScrollState()
        val scope = rememberCoroutineScope()

        val scrollMax = scrollState.maxValue

        val scrollValue by remember { derivedStateOf { scrollState.value } }

        // On any mutation of scroll value, invoke this to reposition scroll
        LaunchedEffect(key1 = scrollValue) {

        }
        Column(
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
                        text = column.first.uppercase(),
                        modifier = Modifier.weight(1f),
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
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

                            // Compute async off of UI thread

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
                                    topLeft = scrollPosition ?: Offset(0f, 0f),
                                    //   topLeft = scrollPosition(
                                    //      tableWidth = canvasWidth,
                                    //     canvasHeight = canvasHeight,
                                    //    value = scrollValue,
                                    //     maxValue = scrollMax
                                    //  ),
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
                                checked = selectionList[i],
                                modifier = Modifier.weight(0.25f),
                                onCheckedChange = {
                                    //  selectedList[i] = it
                                    onSelectIndex(i)
                                })
                            for (column in columns) {
                                val field = rows[i][column.first]
                                Text(
                                    text = field ?: "ERR",
                                    fontSize = 24.sp,
                                    fontFamily = Lato,
                                    fontWeight = if (column.second)
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