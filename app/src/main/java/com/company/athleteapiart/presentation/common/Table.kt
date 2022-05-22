package com.company.athleteapiart.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.presentation.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Table {

    companion object {

        // Constant representing scrollbar width
        private val SCROLLBAR_WIDTH = 8.dp

        @Composable
        fun TableComposable(
            modifier: Modifier,
            columns: List<String>,
            rows: List<List<String>>,
            selectionList: List<Boolean>,
            onSelectIndex: (Int) -> Unit
        ) {
            // Create ScrollState and scope
            val listState = rememberLazyListState()
            val scope = rememberCoroutineScope()

            // We want to recompose on every scroll value change
            val scrollbarWidth = LocalDensity.current.run { SCROLLBAR_WIDTH.toPx() }


            BoxWithConstraints(modifier = modifier) {

                // Make these and all other possibly computations that are constant by remember...
                val tableHeight = LocalDensity.current.run { maxHeight.toPx() }
                val tableWidth = LocalDensity.current.run { maxWidth.toPx() }

                val scrollPosition = remember {
                    mutableStateOf(Offset.Zero)
                }

                // On any mutation of scroll value, invoke this to reposition scroll

                // Row comprised of rows :: scrollbar
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Rows
                    Column(
                        modifier = Modifier.width((tableWidth - scrollbarWidth).dp)
                    ) {

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth(),
                            state = listState
                        ) {
                            item() {
                                TableHeader(
                                    columns = columns
                                )
                            }
                            items(count = rows.size) { index ->
                                TableRow(
                                    enabled = selectionList[index],
                                    fields = rows[index],
                                    onChecked = {
                                        onSelectIndex(index)
                                    })

                            }
                        }
                    }

                    val scrollbarSize = remember { mutableStateOf(Size.Zero) }
                    /*
                    LaunchedEffect(key1 = listState.firstVisibleItemIndex) {
                        println("Launched effect invoked on ${listState.firstVisibleItemIndex}")
                        println("""
                            ${listState.layoutInfo.totalItemsCount} total items
                            ${listState.layoutInfo.viewportEndOffset} end offset
                            ${listState.layoutInfo.viewportStartOffset} start offset
                            ${listState.firstVisibleItemScrollOffset} first visibe item scroll ofset
                            ${listState.interactionSource}
                        """.trimIndent())
                    }

                     */
                    /*
                    LaunchedEffect(key1 = listState., key2 = (scrollState.value / 10)) {
                        println("Launched effect invoked")
                        scope.launch(Dispatchers.Default) {
                            scrollbarSize.value = scrollSize(
                                canvasHeight = tableHeight,
                                scrollWidth = scrollbarWidth,
                                maxValue = scrollState.maxValue
                            )
                            scrollPosition.value = scrollPosition(
                                tableWidth = scrollbarWidth,
                                canvasHeight = tableHeight,
                                scrollbarWidth = scrollbarWidth,
                                value = scrollState.value,
                                maxValue = scrollState.maxValue
                            )
                        }
                    }

                     */
                    println("Here, scroll position is $scrollPosition")
                    println("Scroll bar size is $scrollbarSize")
              //      println("max value is ${scrollState.maxValue}")
                    println("canvas height is $tableHeight")

                    Spacer(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(SCROLLBAR_WIDTH)
                            .background(Silver)
                            .drawBehind {
                                drawRect(
                                    color = Pumpkin,
                                    topLeft = scrollPosition.value,
                                    size = scrollbarSize.value,
                                )
                            }
                    )
                    // }
                }
            }
        }

        @Composable
        private fun TableHeader(
            columns: List<String>
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp) // If hard coded may have solution to scroll size
                    .background(Silver)
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.CheckBox,
                    tint = Asphalt,
                    modifier = Modifier.weight(0.25f),
                    contentDescription = ""
                )
                for (column in columns)
                    Text(
                        text = column.uppercase(),
                        modifier = Modifier.weight(1f),
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        color = Coal,
                        fontWeight = FontWeight.Bold
                    )
            }
        }

        @Composable
        private fun TableRow(
            enabled: Boolean,
            fields: List<String>,
            onChecked: () -> Unit
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = enabled,
                    modifier = Modifier.weight(0.25f),
                    onCheckedChange = {
                        onChecked()
                    })
                for (field in fields) {
                    Text(
                        text = field,
                        fontSize = 24.sp,
                        fontFamily = Lato,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }


        // Invoked in Composable to help build ScrollBar with Canvas
        private fun scrollPosition(
            tableWidth: Float,
            canvasHeight: Float,
            scrollbarWidth: Float,
            value: Int,
            maxValue: Int
        ) = Offset(
            x = -scrollbarWidth,
            y = 0f + ((value.toFloat() / maxValue.toFloat()) * (canvasHeight - scrollSize(
                canvasHeight = canvasHeight,
                scrollWidth = scrollbarWidth,
                maxValue = maxValue
            ).height))
        )


        private fun scrollSize(canvasHeight: Float, scrollWidth: Float, maxValue: Int) = Size(
            width = scrollWidth,
            height = ((1f / maxValue) * 120f) * canvasHeight
        )

    }
}
