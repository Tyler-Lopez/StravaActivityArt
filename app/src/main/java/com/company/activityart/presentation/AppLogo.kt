package com.company.activityart.presentation

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.company.activityart.R

@Composable
fun AppLogo() {
    Image(
        painter = painterResource(id = R.drawable.ic_activity_art_logo),
        contentDescription = stringResource(id = R.string.app_logo_content_description)
    )
}