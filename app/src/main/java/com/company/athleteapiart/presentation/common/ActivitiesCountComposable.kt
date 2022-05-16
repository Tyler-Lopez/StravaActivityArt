package com.company.athleteapiart.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.presentation.ui.theme.Lato
import com.company.athleteapiart.presentation.ui.theme.StravaOrange

@Composable
fun ActivitiesCountComposable(
    count: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "%,d".format(count),
            fontFamily = Lato,
            fontWeight = FontWeight.Bold,
            color = StravaOrange,
            fontSize = 28.sp
        )
        Text(
            text = "Activities Selected",
            fontFamily = Lato,
            fontSize = 24.sp
        )
    }
}