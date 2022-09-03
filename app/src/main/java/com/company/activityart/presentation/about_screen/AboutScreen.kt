package com.company.activityart.presentation.about_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.viewmodel.compose.viewModel
import com.company.activityart.R
import com.company.activityart.architecture.Router
import com.company.activityart.presentation.MainDestination
import com.company.activityart.presentation.common.AppBarScaffold
import com.company.activityart.presentation.common.AppVersionNameComposable
import com.company.activityart.presentation.common.ContainerColumn
import com.company.activityart.presentation.common.type.TitleFour
import com.company.activityart.presentation.common.type.TitleOne
import com.company.activityart.presentation.common.type.TitleThree
import com.company.activityart.presentation.about_screen.AboutScreenViewEvent.*
import com.company.activityart.presentation.about_screen.AboutScreenViewState.*
import com.company.activityart.presentation.about_screen.composables.AboutScreenStandby
import com.company.activityart.presentation.common.ScreenBackground
import com.company.activityart.presentation.ui.theme.*
import com.company.activityart.presentation.welcome_screen.WelcomeScreenViewState
import com.company.activityart.presentation.welcome_screen.composables.WelcomeScreenStandby

@Composable
fun AboutScreen(
    router: Router<MainDestination>,
    viewModel: AboutScreenViewModel = viewModel()
) {
    viewModel.apply {
        attachRouter(router)
        AppBarScaffold(
            text = stringResource(R.string.action_bar_about_header),
            onNavigateUp = { viewModel.onEventDebounced(NavigateUpClicked) }
        ) {
            ScreenBackground(spacing.medium) {
                viewState.collectAsState().value?.let {
                    when (it) {
                        is Standby -> AboutScreenStandby()
                    }
                }
            }
        }
    }
}