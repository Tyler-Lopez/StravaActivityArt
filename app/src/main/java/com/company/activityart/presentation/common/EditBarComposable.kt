package com.company.activityart.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.activityart.presentation.ui.theme.*

@Composable
fun EditBarComposable(
    display: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit
) {
    /*
    val expanded = remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .height(54.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Silver),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(Asphalt)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AspectRatio,
                contentDescription = null,
                tint = Silver,
                modifier = Modifier.size(32.dp)
            )
        }


        Text(
            text = display,
            fontSize = 20.sp,
            fontFamily = Lato,
            fontWeight = FontWeight.SemiBold,
            color = Black,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(StravaOrange)
                .clickable { onEdit() }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(32.dp)
            )
        }

    }

     */
}