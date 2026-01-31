package com.github.freshmorsikov.moviematcher.core.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.github.freshmorsikov.moviematcher.core.ui.theme.color.ColorScheme
import com.github.freshmorsikov.moviematcher.core.ui.theme.color.DarkColorScheme
import com.github.freshmorsikov.moviematcher.core.ui.theme.color.LightColorScheme

val LocalMovieColors = staticCompositionLocalOf { LightColorScheme }
val LocalMovieTypography = staticCompositionLocalOf { MovieTypography() }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val rememberedColors = remember {
        colorScheme.copy()
    }

    CompositionLocalProvider(
        LocalMovieColors provides rememberedColors,
        LocalMovieTypography provides movieTypography(),
        content = content
    )
}

object MovieTheme {
    val colors: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalMovieColors.current
    val typography: MovieTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalMovieTypography.current
}