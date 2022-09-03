package com.company.activityart.presentation.about_screen.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import com.company.activityart.R
import com.company.activityart.presentation.common.type.Body
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.common.type.TitleOne
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.util.Constants

@Composable
fun AboutScreenStandby() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.small)
    ) {
        Subhead(text = "${Constants.STAGE} ${Constants.VERSION}")
        TitleOne(text = Constants.APP_NAME)
    }
    stringArrayResource(id = R.array.about_screen_content).forEach {
        Body(text = it, modifier = Modifier.padding(horizontal = spacing.medium))
    }
}