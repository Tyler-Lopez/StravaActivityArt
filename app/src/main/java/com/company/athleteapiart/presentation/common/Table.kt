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
import kotlin.math.pow

class Table {

    companion object {

        // Constant representing scrollbar width
        private val SCROLLBAR_WIDTH = 8.dp
        private val ROW_HEIGHT = 75.dp

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
            val rowHeightFloat = LocalDensity.current.run { ROW_HEIGHT.toPx() }


            BoxWithConstraints(modifier = modifier) {

                val scrollPosition = remember { mutableStateOf(Offset.Zero) }
                val scrollbarSize = remember {
                    mutableStateOf(
                        Size.Zero
                    )
                }

                // On any mutation of scroll value, invoke this to reposition scroll

                // Row comprised of rows :: scrollbar
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Rows
                    Column {
                        TableHeader(columns = columns)
                        BoxWithConstraints(modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                drawRect(
                                    color = Pumpkin,
                                    topLeft = scrollPosition.value,
                                    size = scrollbarSize.value,
                                )
                            }) {

                            val rowsHeight = LocalDensity.current.run { maxHeight.toPx() }
                            val rowsWidth = LocalDensity.current.run { maxWidth.toPx() }

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                state = listState,
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                items(count = rows.size) { index ->
                                    TableRow(
                                        enabled = selectionList[index],
                                        fields = rows[index],
                                        onChecked = {
                                            onSelectIndex(index)
                                        })

                                }
                            }

                            LaunchedEffect(key1 = listState.firstVisibleItemIndex) {
                                scope.launch(Dispatchers.Default) {
                                    println("KEY 1 IS ${listState.firstVisibleItemIndex} KEY 2 IS ${listState.layoutInfo.viewportStartOffset}")
                                    println("Launched effect invoked on ${listState.firstVisibleItemIndex}")
                                    println("Visible items size is ${listState.layoutInfo.visibleItemsInfo.size}")
                                    println("visible items lastindex is ${listState.layoutInfo.visibleItemsInfo.lastIndex}")
                                    val visibleItemsSize =
                                        listState.layoutInfo.visibleItemsInfo.size.toDouble()
                                    val size = rows.size.toFloat()

                                    scrollbarSize.value = Size(
                                        width = scrollbarWidth,
                                        // (x / y) * (50 * x)
                                        height = listState.layoutInfo.viewportEndOffset
                                                / ((size * rowHeightFloat)) * (rowsHeight)
                                    )
                                    scrollPosition.value = Offset(
                                        x = rowsWidth - scrollbarWidth,
                                        // 300 - 0 = 300
                                        y = (listState.layoutInfo.viewportEndOffset * ((listState.firstVisibleItemIndex.toFloat() + 1) / (size))) - rowHeightFloat
                                    )
                                }
                            }
                        }
                    }
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
                    .height(ROW_HEIGHT) // If hard coded may have solution to scroll size
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ROW_HEIGHT),
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
    }
}
