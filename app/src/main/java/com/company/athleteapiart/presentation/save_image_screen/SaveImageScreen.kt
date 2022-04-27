package com.company.athleteapiart.presentation.save_image_screen

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.company.athleteapiart.presentation.composable.*
import com.company.athleteapiart.presentation.ui.spacing
import com.company.athleteapiart.presentation.ui.theme.StravaOrange
import com.company.athleteapiart.util.Constants

@Composable
fun SaveImageScreen(
    navController: NavHostController,
    viewModel: SaveImageViewModel = hiltViewModel()
) {

    val isLoading by remember { viewModel.isLoading }
    val endReached by remember { viewModel.endReached }
    val filePath by remember { viewModel.imageSavedAt }


    Scaffold(
        topBar = {
            ComposableTopBar(
                leftContent = {
                    ComposableReturnButton(onClick = {
                        navController.navigateUp()
                    })
                },
                rightContent = {
                    ComposableSubtext(
                        text = "",
                        color = Color.White
                    )
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                when {
                    !isLoading && !endReached -> viewModel.startSave(LocalContext.current)
                    isLoading && !endReached -> {

                        CircularProgressIndicator(
                            color = MaterialTheme.colors.primary
                        )
                        Row(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            ComposableHeader(
                                text = "Saving Image",
                                isBold = true
                            )
                        }

                    }
                    endReached -> {
                        Icon(
                            Icons.Default.Check,
                            "",
                            tint = StravaOrange,
                            modifier = Modifier.size(64.dp)
                        )
                        ComposableHeader(
                            text = "Image Saved",
                            isBold = true
                        )
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            ComposableSubtext(text = "Pictures/" + Constants.IMAGE_DIRECTORY, modifier = Modifier.padding(vertical = MaterialTheme.spacing.sm))
                        }
                    }
                }
            }
        }
    )
}
