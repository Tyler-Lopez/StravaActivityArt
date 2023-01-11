package com.activityartapp.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.activityartapp.presentation.common.type.TitleFour
import com.activityartapp.R

@Composable
fun AppBarScaffold(
    text: String,
    onNavigateUp: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    tabLayout: @Composable () -> Unit = {},
    snackbarHostState: SnackbarHostState? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val scaffoldState =
        rememberScaffoldState(snackbarHostState = snackbarHostState ?: SnackbarHostState())

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        TitleFour(
                            text = text,
                            textColor = colorResource(id = R.color.white)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onNavigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.navigate_up_cd),
                                tint = Color.White
                            )
                        }
                    },
                    actions = actions,
                )
                tabLayout()
            }
        },
        content = content
    )
}