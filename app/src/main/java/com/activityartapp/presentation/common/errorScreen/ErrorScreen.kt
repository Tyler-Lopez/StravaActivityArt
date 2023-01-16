package com.activityartapp.presentation.common.errorScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.presentation.common.CardColumn
import com.activityartapp.presentation.common.button.ButtonSize
import com.activityartapp.presentation.common.button.HighEmphasisButton
import com.activityartapp.presentation.common.button.MediumEmphasisButton
import com.activityartapp.presentation.common.type.Body
import com.activityartapp.presentation.common.type.Subhead
import com.activityartapp.presentation.common.type.SubheadHeavy
import com.activityartapp.presentation.common.type.TitleTwo

@Composable
fun ColumnScope.ErrorScreen(
    header: String,
    description: String,
    prompt: String,
    retrying: Boolean,
    onContinueClicked: (() -> Unit)? = null,
    onRetryClicked: (() -> Unit)? = null,
    onReturnClicked: (() -> Unit)? = null
) {
    //TitleTwo(text = stringResource(R.string.loading_activities_error_header))
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            tint = colorResource(R.color.n90_coal),
            contentDescription = null
        )
        TitleTwo(text = header)
    }
    CardColumn {
        Body(text = prompt, textAlign = TextAlign.Center)
        Body(text = description, textAlign = TextAlign.Center)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        onContinueClicked?.let {
            HighEmphasisButton(
                size = ButtonSize.LARGE,
                enabled = !retrying,
                text = stringResource(R.string.error_continue_button),
                onClick = it
            )
        }
        onRetryClicked?.let {
            MediumEmphasisButton(
                size = ButtonSize.LARGE,
                enabled = !retrying,
                isLoading = retrying,
                text = stringResource(R.string.error_retry_button),
                onClick = it
            )
        }
        onReturnClicked?.let {
            MediumEmphasisButton(
                size = ButtonSize.LARGE,
                enabled = !retrying,
                text = stringResource(R.string.error_return_button),
                onClick = it
            )
        }
    }
}