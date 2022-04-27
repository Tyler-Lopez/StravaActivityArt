package com.company.athleteapiart.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.company.athleteapiart.presentation.ui.spacing
import com.company.athleteapiart.presentation.ui.theme.*

@Composable
fun ComposableShadowBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxWithConstraintsScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

        val isThin = this.maxWidth < 414.dp

        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
                .background(
                    brush = Brush
                        .verticalGradient(
                            colors = listOf(
                                WarmGrey60,
                                WarmGrey70
                            )
                        ),
                )
                .border(
                    width = if (isThin) 0.dp else MaterialTheme.spacing.xs,
                    color = WarmGrey50
                )
                .verticalScroll(scrollState)
                .padding(bottom = if (isThin) 64.dp else 0.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}
