package com.company.activityart.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.activityart.presentation.ui.theme.*
import com.company.activityart.presentation.visualize_screen.ResolutionSpec

@Composable
fun DropdownComposable(
    menuItems: List<ResolutionSpec>,
    message: String,
    onItemSelected: (Int) -> Unit,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    height: Dp = 44.dp,
) {
    /*
    val expanded = remember { mutableStateOf(false) }

    println("Recomposed, selected index is $selectedIndex")
    println("Recomposed, selected display is ${menuItems.get(selectedIndex).display}")
    Row(
        modifier = modifier
            .height(height)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(Asphalt)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                fontFamily = MaisonNeue,
                fontSize = 24.sp,
                color = Fog
            )
        }
        BoxWithConstraints {
            val maxWidth = this.maxWidth
            val maxHeight = this.maxHeight

            BoxWithConstraints(
                modifier = Modifier
                    .width(maxWidth)
                    .height(maxHeight)
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
                        text = menuItems.get(selectedIndex).display,
                        fontSize = 20.sp,
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
                        if (index != selectedIndex)
                            DropdownMenuItem(onClick = {
                                expanded.value = false
                                onItemSelected(index)
                            }) {
                                Text(
                                    text = item.display,
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

     */
}