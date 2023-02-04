package com.activityartapp.presentation.common.textField

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MediumEmphasisTextField(
    text: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    imageVector: ImageVector? = null,
    contentDescription: String? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    /*
    OutlinedTextField(
        value = text,
        modifier = modifier,
        label = {
            SubheadHeavy(text = text)
        },
        onValueChange = {

        }
    )

     */
}