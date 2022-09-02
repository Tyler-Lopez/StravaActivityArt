package com.company.activityart.presentation.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.company.activityart.R

val MaisonNeue = FontFamily(
    Font(R.font.maisonneue_bold, FontWeight.Bold),
    Font(R.font.maisonneue_demi, FontWeight.Medium),
    Font(R.font.maisonneue_book, FontWeight.Normal),
)

/**
 * Normal == book
 * Medium == demi
 * Bold == bold
 */
// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = MaisonNeue,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    h1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 34.sp,
        color = LightTextPrimary
    ),
    h2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 28.sp,
        color = LightTextPrimary
    ),
    subtitle1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        color = LightTextSecondary
    ),
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)