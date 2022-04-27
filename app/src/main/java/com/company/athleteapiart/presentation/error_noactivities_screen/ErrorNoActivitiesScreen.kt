package com.company.athleteapiart.presentation.error_noactivities_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.company.athleteapiart.Screen
import com.company.athleteapiart.presentation.composable.*
import com.company.athleteapiart.presentation.ui.spacing
import com.company.athleteapiart.presentation.ui.theme.StravaOrange

@Composable
fun ErrorNoActivitiesScreen(
    navController: NavHostController
) {
    BackHandler {
        navController.backQueue.clear()
        navController.navigate(Screen.TimeSelect.route)
    }
    Scaffold(
        topBar = {
            ComposableTopBar(
                leftContent = {
                    ComposableReturnButton(onClick = {
                        navController.navigate(Screen.TimeSelect.route)
                    })
                },
                rightContent = {
                    ComposableSubtext(
                        text = "Error",
                        color = Color.White
                    )
                }
            )
        },
        content = {
            ComposableScreenWrapper {
                Icon(
                    Icons.Default.Error,
                    "",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(MaterialTheme.spacing.xxs),
                    tint = StravaOrange
                )
                ComposableHeader(
                    text = "Oops!",
                    isBold = true,
                    modifier = Modifier.padding(MaterialTheme.spacing.xxs)
                )
                ComposableParagraph(
                    text = "No activities were found for this selection",
                    modifier = Modifier.padding(MaterialTheme.spacing.xxs)
                )
            }

        }
    )
}
