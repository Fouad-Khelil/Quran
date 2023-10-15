package com.example.quran.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.quran.R

// Set of Material typography styles to start with

val noto_arabic_font = FontFamily(Font(R.font.noto_kufi_arabic_regular) )
val hafs_uthmanic_font = FontFamily( Font(R.font.uthmanic_hafs_v20))
val thulth_font = FontFamily( Font(R.font.thulth_regular))


val Typography = Typography(
    body1 = TextStyle(
        fontFamily = hafs_uthmanic_font,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
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