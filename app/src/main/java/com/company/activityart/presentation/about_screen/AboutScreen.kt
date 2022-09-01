package com.company.activityart.presentation.about_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.company.activityart.presentation.common.AppVersionNameComposable
import com.company.activityart.presentation.common.ContainerColumn
import com.company.activityart.presentation.ui.theme.*

@Composable
fun AboutScreen() {

    val scrollState = rememberScrollState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Icicle),
        contentAlignment = Alignment.Center
    ) {
        ContainerColumn(maxWidth) {
            Box(
                modifier = Modifier
            ) {
                AppVersionNameComposable()
            }
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .background(Silver)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp)),
            ) {
                val text = """
                This app lets athletes create digital pictures of their Strava activities at a very high resolution appropriate for printing and framing.
                
                As an athlete might have thousands of activities, they can choose which to make art from based on the activity date, type, gear, and distance.
           
                Activity Art is still in development; please send suggestions or any bugs found to bunsendevelopment@gmail.com
            """.trimIndent()
                val start = text.indexOf("bunsendevelopment@gmail.com")
                val spanStyles = listOf(
                    AnnotatedString.Range(
                        SpanStyle(
                            fontFamily = MaisonNeue,
                            fontWeight = FontWeight.Bold,
                            color = StravaOrange
                        ),
                        start = start,
                        end = start + "bunsendevelopment@gmail.com".length
                    )
                )
                /*
                Text(
                    text = AnnotatedString(
                        text = text,
                        spanStyles = spanStyles,
                    ),
                    textAlign = TextAlign.Center,
                    fontFamily = Lato,
                    fontSize = 22.sp,
                    lineHeight = 28.sp
                )

                 */
            }

        }
    }
}