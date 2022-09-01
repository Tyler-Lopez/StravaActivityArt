package com.company.activityart.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ButtonWithCountComposable(
    activitiesEmpty: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    /*
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

     */

}