package com.company.activityart.presentation.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.activityart.presentation.ui.theme.*

class Table {

    companion object {

        // Constant representing scrollbar width
        private const val SCROLLBAR_WIDTH = 8f
        private const val ROW_HEIGHT = 65f
        private const val BORDER_WIDTH = 0f

        @Composable
        fun TableComposable(
            modifier: Modifier,
            columns: List<String>,
            rows: List<List<Pair<String, Boolean>>>,
            selectionList: List<Boolean>,
            onSelectIndex: (Int) -> Unit
        ) {
            // Create ScrollState and scope
            val state = rememberLazyListState()

            BoxWithConstraints(modifier = modifier) {
                // Row comprised of rows :: scrollbar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(with(LocalDensity.current) { BORDER_WIDTH.toDp() }, Silver)
                ) {
                    // Rows
                    Column {
                        TableHeader(columns = columns)
                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(White)
                        ) {

                            val boxHeight = maxHeight
                            val rowsHeight =
                                LocalDensity.current.run { maxHeight.roundToPx() }.toFloat()
                            val rowsWidth =
                                LocalDensity.current.run { maxWidth.roundToPx() }.toFloat()
                            val rowsSzFl = remember { rows.size.toFloat() }
                            val displayScrollbar = remember {
                                derivedStateOf { state.layoutInfo.totalItemsCount != state.layoutInfo.visibleItemsInfo.size }
                            }

                            val first = remember { derivedStateOf { state.firstVisibleItemIndex.toFloat() } }
                            val offset = remember { derivedStateOf { state.firstVisibleItemScrollOffset.toFloat() } }

                            val scrollPosition = remember {
                                derivedStateOf {
                                    Offset(
                                        x = rowsWidth - SCROLLBAR_WIDTH - BORDER_WIDTH,
                                        y = (((first.value * ROW_HEIGHT) + offset.value) / (rowsSzFl * ROW_HEIGHT)) * rowsHeight
                                    )
                                }
                            }


                            val scrollbarSize = remember {
                                mutableStateOf(
                                    Size(
                                        width = SCROLLBAR_WIDTH,
                                        height = (rowsHeight / (rowsSzFl * ROW_HEIGHT)) * rowsHeight
                                    )
                                )
                            }

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                state = state
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

                            if (displayScrollbar.value)
                                Canvas(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(boxHeight)
                                ) {
                                    drawRect(
                                        color = Asphalt,
                                        topLeft = scrollPosition.value,
                                        size = scrollbarSize.value,
                                    )
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
                 //   .height(with(LocalDensity.current) { ROW_HEIGHT.toDp() }) // If hard coded may have solution to scroll size
                    .background(Silver)
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.CheckBox,
                    tint = Coal,
                    modifier = Modifier
                        .weight(0.25f),
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
            fields: List<Pair<String, Boolean>>,
            onChecked: () -> Unit
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = enabled,
                    modifier = Modifier
                        .weight(0.25f),
                    onCheckedChange = {
                        onChecked()
                    })
                /*
                for (field in fields) {
                    Text(
                        text = field.first,
                        fontSize = 24.sp,
                        fontFamily = Lato,
                        fontWeight = if (field.second) FontWeight.Bold else FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }

                 */
            }
        }
    }
}
