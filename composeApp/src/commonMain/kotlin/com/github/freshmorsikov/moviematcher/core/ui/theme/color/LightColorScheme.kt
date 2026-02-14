package com.github.freshmorsikov.moviematcher.core.ui.theme.color

import androidx.compose.ui.graphics.Color.Companion.Black

val LightColorScheme = ColorScheme(
    primary = Lavender,
    primaryDisabled = LavenderGray,
    background = Grey500,
    stroke = Grey400,
    warning = Yellow,
    error = Red,
    surface = SurfaceColors(
        main = White,
        variant = Grey300,
    ),
    icon = IconColors(
        main = Black,
        variant = Grey300,
    ),
    shimmer = ShimmerColors(
        container = White,
        content = Grey400,
    ),
    text = TextColors(
        main = Blck,
        variant = Grey200,
        accent = Amber,
        onAccent = White,
        onWarning = DarkYellow,
    ),
)
