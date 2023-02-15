package com.activityartapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.activityartapp.R
import com.activityartapp.presentation.common.layout.ColumnSmallSpacing

@Composable
fun AppLogo() {
    ColumnSmallSpacing {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            modifier = Modifier.requiredSizeIn(maxWidth = 64.dp),
            contentDescription = stringResource(id = R.string.app_logo_content_description)
        )
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.h5
        )
    }
}