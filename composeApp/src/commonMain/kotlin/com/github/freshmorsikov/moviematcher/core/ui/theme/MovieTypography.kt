package com.github.freshmorsikov.moviematcher.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import org.jetbrains.compose.resources.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import moviematcher.composeapp.generated.resources.Res
import moviematcher.composeapp.generated.resources.roboto_regular

val robotoFontFamily
    @Composable
    get() = FontFamily(
        Font(
            resource = Res.font.roboto_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        )
    )

@Immutable
data class MovieTypography(
    val title20: TextStyle = TextStyle.Default,
    val title16: TextStyle = TextStyle.Default,
    val action16: TextStyle = TextStyle.Default,
    val body14: TextStyle = TextStyle.Default,
    val label12: TextStyle = TextStyle.Default,
)

@Composable
fun movieTypography(): MovieTypography {
    return MovieTypography(
        title20 = TextStyle(
            fontFamily = robotoFontFamily,
            fontWeight = FontWeight.W700,
            fontSize = 20.sp,
            lineHeight = 23.sp,
        ),
        title16 = TextStyle(
            fontFamily = robotoFontFamily,
            fontWeight = FontWeight.W700,
            fontSize = 16.sp,
            lineHeight = 19.sp,
        ),
        action16 = TextStyle(
            fontFamily = robotoFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 19.sp,
        ),
        body14 = TextStyle(
            fontFamily = robotoFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            lineHeight = 16.sp,
        ),
        label12 = TextStyle(
            fontFamily = robotoFontFamily,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            lineHeight = 14.sp,
        ),
    )
}