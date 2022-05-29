package com.company.activityart.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.activityart.presentation.ui.theme.Lato

@Composable
fun LoadingComposable(
    loadingText: String = "Loading..."
){
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator()
        Text(
            text = loadingText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Lato
        )
    }
}