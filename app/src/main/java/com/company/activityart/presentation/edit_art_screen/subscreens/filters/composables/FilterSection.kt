package com.company.activityart.presentation.edit_art_screen.subscreens.filters.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import com.company.activityart.R
import com.company.activityart.presentation.ui.theme.Typography

@Composable
fun FilterSection() {
    Text(
        text = "Date window includes 200 activities",
        style = Typography.subtitle1,
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.pumpkin)
    )
}