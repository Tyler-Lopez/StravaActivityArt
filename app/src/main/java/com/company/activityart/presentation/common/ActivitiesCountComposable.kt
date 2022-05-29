package com.company.activityart.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.activityart.presentation.ui.theme.Lato
import com.company.activityart.presentation.ui.theme.MaisonNeue
import com.company.activityart.presentation.ui.theme.StravaOrange

@Composable
fun ActivitiesCountComposable(
    count: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "%,d".format(count),
                fontFamily = MaisonNeue,
                fontWeight = FontWeight.Bold,
                color = StravaOrange,
                fontSize = 28.sp
            )
            Text(
                text = "Activities Selected",
                fontFamily = Lato,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

    }
}