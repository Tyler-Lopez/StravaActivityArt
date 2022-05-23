package com.company.athleteapiart.presentation.common

import androidx.compose.foundation.Canvas
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
import kotlin.math.pow

class Table {

    companion object {

        // Constant representing scrollbar width
        private val SCROLLBAR_WIDTH = 4.dp
        private val ROW_HEIGHT = 50.dp

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
            val scrollbarWidth = LocalDensity.current.run { SCROLLBAR_WIDTH.roundToPx() }.toFloat()
            val rowHeightFloat = LocalDensity.current.run { ROW_HEIGHT.roundToPx() }.toFloat()
            val scrollOffsetFl = LocalDensity.current.run {  }

            BoxWithConstraints(modifier = modifier) {

                val scrollPosition = remember { mutableStateOf(Offset.Zero) }

                // On any mutation of scroll value, invoke this to reposition scroll

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
                            val rowsHeight = LocalDensity.current.run { maxHeight.roundToPx() }.toFloat()
                            val rowsWidth = LocalDensity.current.run { maxWidth.roundToPx() }.toFloat()
                            val rowsSzFl = remember { rows.size.toFloat() }

                            val scrollbarSize = remember {
                                mutableStateOf(
                                    Size(
                                        width = scrollbarWidth.toFloat(),
                                        height = (rowsHeight / (rowsSzFl * rowHeightFloat)) * rowsHeight
                                    )
                                )
                            }

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

                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(boxHeight)
                            ) {
                                drawRect(
                                    color = Silver,
                                    topLeft = scrollPosition.value,
                                    size = scrollbarSize.value,
                                )
                            }

                            LaunchedEffect(
                                key1 = listState.firstVisibleItemScrollOffset
                            ) {
                                scope.launch(Dispatchers.Default) {
                                    scrollPosition.value = Offset(
                                        x = rowsWidth - scrollbarWidth,
                                        // 300 - 0 = 300
                                        y = (((listState.firstVisibleItemIndex * rowHeightFloat) +
                                                listState.firstVisibleItemScrollOffset) / (rowsSzFl * rowHeightFloat)) * rowsHeight
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
                    modifier = Modifier
                        .size(
                            ROW_HEIGHT * 0.6f
                        )
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
                    .fillMaxWidth()
                    .height(ROW_HEIGHT),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = enabled,
                    modifier = Modifier
                        .scale(1.25f)
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
