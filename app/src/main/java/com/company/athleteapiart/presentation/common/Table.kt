package com.company.athleteapiart.presentation.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.scale
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
        private val SCROLLBAR_WIDTH = 6f
        private val ROW_HEIGHT = 65f

        @Composable
        fun TableComposable(
            modifier: Modifier,
            columns: List<String>,
            rows: List<List<String>>,
            selectionList: List<Boolean>,
            onSelectIndex: (Int) -> Unit
        ) {
            // Create ScrollState and scope
            val state = rememberLazyListState()
            val scope = rememberCoroutineScope()

            BoxWithConstraints(modifier = modifier) {
                // Row comprised of rows :: scrollbar
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Rows
                    Column {
                        TableHeader(columns = columns)
                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            val boxHeight = maxHeight
                            val rowsHeight =
                                LocalDensity.current.run { maxHeight.roundToPx() }.toFloat()
                            val rowsWidth =
                                LocalDensity.current.run { maxWidth.roundToPx() }.toFloat()
                            val rowsSzFl = remember { rows.size.toFloat() }
                            val displayScrollbar = remember {
                                derivedStateOf {
                                    state.layoutInfo.visibleItemsInfo.size != rows.size
                                }
                            }

                            val scrollPosition = remember {
                                mutableStateOf(
                                    Offset(
                                        x = rowsWidth - SCROLLBAR_WIDTH,
                                        y = 0f
                                    )
                                )
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
                                state = state,
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

                            LaunchedEffect(
                                key1 = state.firstVisibleItemScrollOffset,
                                key2 = state.firstVisibleItemIndex,
                                key3 = state.layoutInfo.visibleItemsInfo.lastIndex
                            ) {
                                scope.launch(Dispatchers.Default) {

                                    val first = state.firstVisibleItemIndex.toFloat()
                                    val last = state.layoutInfo.visibleItemsInfo.lastIndex.toFloat()
                                    val offset = state.firstVisibleItemScrollOffset.toFloat()

                                    scrollPosition.value = Offset(
                                        x = rowsWidth - SCROLLBAR_WIDTH,
                                        // 300 - 0 = 300
                                        y = (((first * ROW_HEIGHT) + offset) / (rowsSzFl * ROW_HEIGHT)) * rowsHeight
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
                    .height(with(LocalDensity.current) { ROW_HEIGHT.toDp() }) // If hard coded may have solution to scroll size
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
            fields: List<String>,
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
