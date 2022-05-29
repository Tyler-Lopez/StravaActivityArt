package com.company.activityart.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.activityart.presentation.ui.theme.MaisonNeue

@Composable
fun ButtonComposable(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    onClick: () -> Unit,
) {
    Button(
        onClick = { if (enabled) onClick() },
        modifier = modifier
            .alpha(if (!enabled) 0.5f else 1f)
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            fontFamily = MaisonNeue,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp
        )
    }

}