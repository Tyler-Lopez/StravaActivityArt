package com.company.athleteapiart.presentation.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.athleteapiart.presentation.activity_visualize_screen.activitiesVisualizeCanvas
import com.company.athleteapiart.ui.theme.Roboto
import com.company.athleteapiart.ui.theme.StravaOrange
import com.company.athleteapiart.ui.theme.White
import com.company.athleteapiart.util.saveImage

@Composable
fun ComposableSaveImageButton(
    onClick: () -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(StravaOrange),
        elevation = ButtonDefaults.elevation(0.dp),
        onClick = {
            onClick()
        }
    ) {
        Text(
            text = "Save",
            fontFamily = Roboto,
            fontSize = 20.sp,
            color = White,
            fontWeight = FontWeight.Medium
        )
        Icon(
            imageVector = Icons.Default.Save,
            contentDescription = "",
            tint = White,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}