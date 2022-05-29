package com.company.activityart.presentation.common

import androidx.compose.material.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.activityart.presentation.ui.theme.Lato

@Composable
fun ButtonWithCountComposable(
    activitiesEmpty: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    if (activitiesEmpty)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = "",
                    tint = Color.Gray
                )
                Text(
                    text = "At least one activity must be selected",
                    fontFamily = Lato,
                    fontSize = 24.sp,
                    color = Color.Gray
                )
            }
        }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ButtonComposable(text = "Continue", enabled = !activitiesEmpty, modifier = Modifier.fillMaxWidth()) { onClick() }
    }

}