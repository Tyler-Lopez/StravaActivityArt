package com.company.athleteapiart.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.company.athleteapiart.presentation.ui.theme.*

@Composable
fun DropdownComposable(
    menuItems: List<String>,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    defaultSelectedIndex: Int = 0,
) {
    val expanded = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(defaultSelectedIndex) }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
    ) {
        Box(modifier = Modifier
            .fillMaxHeight()
            .width(48.dp)
            .background(Pumpkin),
        contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.FormatSize,
                contentDescription = null,
                tint = White
            )
        }
        BoxWithConstraints {
            val maxWidth = this.maxWidth
            val maxHeight = this.maxHeight
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        expanded.value = !expanded.value
                    }
                    .background(Silver)
                    .padding(MenuDefaults.DropdownMenuItemContentPadding),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector =
                        if (expanded.value)
                            Icons.Default.ArrowDropUp
                        else Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Black
                    )
                    Text(
                        text = menuItems.getOrElse(selectedIndex.value) { "ERR" },
                        fontSize = 22.sp,
                        fontFamily = Lato,
                        fontWeight = FontWeight.SemiBold,
                        color = Black,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column {
                Spacer(
                    modifier = Modifier
                        .width(maxWidth)
                        .height(maxHeight)
                )
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                    modifier = Modifier.width(maxWidth)
                ) {
                    menuItems.forEachIndexed { index, item ->
                        if (index != selectedIndex.value)
                            DropdownMenuItem(onClick = {
                                expanded.value = false
                                selectedIndex.value = index
                                onItemSelected(index)
                            }) {
                                Spacer(modifier = Modifier.width(32.dp))
                                Text(
                                    text = item,
                                    fontSize = 18.sp,
                                    fontFamily = Lato,
                                    color = Asphalt,
                                    textAlign = TextAlign.Center
                                )
                            }
                    }
                }
            }
        }
    }
}