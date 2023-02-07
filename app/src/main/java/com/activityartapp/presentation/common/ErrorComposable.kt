package com.activityartapp.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.activityartapp.R
import com.activityartapp.presentation.common.button.Button
import com.activityartapp.presentation.common.button.ButtonEmphasis
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.layout.ColumnMediumSpacing
import com.activityartapp.presentation.common.layout.ColumnSmallSpacing
import com.activityartapp.presentation.ui.theme.spacing

@Composable
fun ErrorComposable(
    header: String,
    description: String,
    prompt: String,
    retrying: Boolean = false,
    onContinueClicked: (() -> Unit)? = null,
    onReconnectStravaClicked: (() -> Unit)? = null,
    onRetryClicked: (() -> Unit)? = null,
    onReturnClicked: (() -> Unit)? = null
) {
    Card {
        ColumnMediumSpacing(modifier = Modifier.padding(spacing.medium)) {
            ColumnSmallSpacing {
                Text(
                    text = header,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5
                )
                Divider()
            }
            ColumnSmallSpacing {
                Text(
                    text = description,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = prompt,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body1
                )
            }

            var hasUsedHighEmphasisButton = false
            var hasUsedMediumEmphasisButton = false

            val determineEmphasis: () -> ButtonEmphasis = {
                when {
                    !hasUsedHighEmphasisButton -> {
                        hasUsedHighEmphasisButton = true
                        ButtonEmphasis.HIGH
                    }
                    !hasUsedMediumEmphasisButton -> {
                        hasUsedMediumEmphasisButton = true
                        ButtonEmphasis.MEDIUM
                    }
                    else -> ButtonEmphasis.LOW
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                onReconnectStravaClicked?.let {
                    Button(
                        emphasis = determineEmphasis(),
                        size = ButtonSize.MEDIUM,
                        enabled = !retrying,
                        text = stringResource(R.string.error_reconnect_button),
                        onClick = onReconnectStravaClicked
                    )
                }
                onContinueClicked?.let {
                    Button(
                        emphasis = determineEmphasis(),
                        size = ButtonSize.MEDIUM,
                        enabled = !retrying,
                        text = stringResource(R.string.error_continue_button),
                        onClick = onContinueClicked
                    )
                }
                onRetryClicked?.let {
                    Button(
                        emphasis = determineEmphasis(),
                        size = ButtonSize.MEDIUM,
                        isLoading = retrying,
                        enabled = !retrying,
                        text = stringResource(R.string.error_retry_button),
                        onClick = onRetryClicked
                    )
                }
                onReturnClicked?.let {
                    Button(
                        emphasis = determineEmphasis(),
                        size = ButtonSize.MEDIUM,
                        enabled = !retrying,
                        text = stringResource(R.string.error_return_button),
                        onClick = onReturnClicked
                    )
                }
            }
        }
    }
}