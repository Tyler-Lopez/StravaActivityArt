package com.company.athleteapiart

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.company.athleteapiart.presentation.NavGraphs
import com.company.athleteapiart.presentation.activity_visualize_screen.ActivitiesScreen
import com.company.athleteapiart.presentation.composable.ComposableTopBar
import com.company.athleteapiart.presentation.login_screen.LoginScreen
import com.company.athleteapiart.presentation.time_select_screen.TimeSelectScreen
import com.company.athleteapiart.ui.theme.AthleteApiArtTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.manualcomposablecalls.ManualComposableCallsBuilder
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Create view model with URI if received

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)




        setContent {
            AthleteApiArtTheme {
                val viewModel = MainViewModel(uri = intent.data)
                var isLoading by remember { mutableStateOf(true) }

                if (isLoading)
                    LoginScreen(onClick = { intent ->
                        if (intent == null) {
                            isLoading = false
                        } else {
                            startActivity(intent)
                        }
                    })
                else {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
    }
}