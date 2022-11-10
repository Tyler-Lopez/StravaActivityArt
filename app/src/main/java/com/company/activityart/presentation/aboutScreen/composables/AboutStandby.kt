package com.company.activityart.presentation.aboutScreen.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.company.activityart.R
import com.company.activityart.presentation.common.type.Body
import com.company.activityart.presentation.common.type.Subhead
import com.company.activityart.presentation.common.type.TitleOne
import com.company.activityart.presentation.ui.theme.spacing
import com.company.activityart.util.constants.StringConstants.STAGE
import com.company.activityart.util.constants.StringConstants.VERSION

@Composable
fun AboutScreenStandby() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.small),
        modifier = Modifier.padding(top = spacing.medium)
    ) {
        Subhead(text = "$STAGE $VERSION")
        TitleOne(text = stringResource(R.string.app_name))
    }
    stringArrayResource(id = R.array.about_screen_content).forEach {
        Body(text = it, modifier = Modifier.padding(horizontal = spacing.medium))
    }
}