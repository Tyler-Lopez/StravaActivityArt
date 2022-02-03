package com.company.athleteapiart.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.company.athleteapiart.ui.spacing
import com.company.athleteapiart.ui.theme.WarmGrey30
import com.company.athleteapiart.ui.theme.WarmGrey40
import com.company.athleteapiart.ui.theme.WarmGrey50

@Composable
fun ComposableShadowBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxWithConstraintsScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush
                    .verticalGradient(
                        colors = listOf(
                            WarmGrey30,
                            WarmGrey40
                        )
                    ),
            )
            .border(
                width = MaterialTheme.spacing.xs,
                color = WarmGrey50
            )
            .verticalScroll(scrollState),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
