package com.company.activityart.presentation.saveArtScreen

import android.Manifest
import android.R.attr.bitmap
import android.content.Intent
import android.content.Intent.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import com.company.activityart.BuildConfig
import com.company.activityart.architecture.EventReceiver
import com.company.activityart.presentation.common.button.ButtonSize
import com.company.activityart.presentation.common.button.HighEmphasisButton
import com.company.activityart.presentation.common.button.MediumEmphasisButton
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.io.FileOutputStream


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SaveArtStandby(
    bitmapScreenSize: Bitmap,
    buttonsEnabled: Boolean,
    downloadInProgress: Boolean,
    eventReceiver: EventReceiver<SaveArtViewEvent>,
    snackbarHostState: SnackbarHostState
) {
    val osGreaterThan10 = BuildConfig.VERSION_CODE > Build.VERSION_CODES.Q
    val permissionState = rememberPermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            Image(
                bitmap = bitmapScreenSize.asImageBitmap(),
                modifier = Modifier.weight(1f, false),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Column {
                HighEmphasisButton(
                    enabled = buttonsEnabled,
                    isLoading = downloadInProgress,
                    size = ButtonSize.MEDIUM,
                    text = "Download"
                ) {
                    /** For Android <= 10, permission must be requested **/
                    permissionState.apply {
                        when {
                            osGreaterThan10 || hasPermission -> {
                                eventReceiver.onEventDebounced(SaveArtViewEvent.ClickedDownload)
                            }
                            !permissionRequested || shouldShowRationale -> {
                                permissionState.launchPermissionRequest()
                            }
                            else -> {
                                println("perma denied")
                            }
                        }
                    }
                }
                val context = LocalContext.current
                MediumEmphasisButton(
                    enabled = buttonsEnabled,
                    size = ButtonSize.MEDIUM,
                    text = "Share"
                ) {
                    eventReceiver.onEventDebounced(SaveArtViewEvent.ClickedShare)
                    /*
                    val imagefolder = File(context.getCacheDir(), "images")
                    var uri: Uri? = null
                    try {
                        imagefolder.mkdirs()
                        val file = File(imagefolder, "shared_image.png")
                        val outputStream = FileOutputStream(file)
                        bitmapScreenSize.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                        outputStream.flush()
                        outputStream.close()
                        uri = FileProvider.getUriForFile(
                            context,
                            "com.company.activityart",
                            file
                        )
                        println("Here, uri is $uri")
                    } catch (e: Exception) {
                        println("Caught exception $e")
                     //   Toast.makeText(this, "" + e.message, Toast.LENGTH_LONG).show()
                    }
                  //  return uri
                    val intent = Intent(ACTION_SEND)
                    intent.putExtra(EXTRA_STREAM, uri)
                    intent.putExtra(EXTRA_TEXT, "Sharing Image")
                    intent.putExtra(EXTRA_SUBJECT, "Subject Here")
                    intent.type = "image/png"
                    context.startActivity(Intent.createChooser(intent, "Share Via"))
                   // eventReceiver.onEventDebounced(SaveArtViewEvent.ClickedShare)

                     */
                }
            }
        }
        SnackbarHost(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            hostState = snackbarHostState
        )
    }
}