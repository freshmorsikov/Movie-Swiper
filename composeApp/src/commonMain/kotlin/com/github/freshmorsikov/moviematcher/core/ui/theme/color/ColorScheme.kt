package com.github.freshmorsikov.moviematcher.core.ui.theme.color

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Stable
class ColorScheme(
    primary: Color,
    background: Color,
    surface: Color,
    icon: Color,
    error: Color,
    text: TextColors,
) {

    var primary by mutableStateOf(primary)
        internal set

    var background by mutableStateOf(background)
        internal set

    var surface by mutableStateOf(surface)
        internal set

    var icon by mutableStateOf(icon)
        internal set

    var error by mutableStateOf(error)
        internal set

    var text by mutableStateOf(text)
        internal set

    fun copy(
        primary: Color = this.primary,
        background: Color = this.background,
        surface: Color = this.surface,
        icon: Color = this.icon,
        error: Color = this.error,
        text: TextColors = this.text,
    ): ColorScheme = ColorScheme(
        primary = primary,
        background = background,
        surface = surface,
        icon = icon,
        error = error,
        text = text.copy(
            main = text.main,
            variant = text.variant,
            onAccent = text.onAccent,
        ),
    )
}

@Stable
class TextColors(
    main: Color,
    variant: Color,
    onAccent: Color,
) {
    var main by mutableStateOf(main)
        internal set

    var variant by mutableStateOf(variant)
        internal set

    var onAccent by mutableStateOf(onAccent)
        internal set

    fun copy(
        main: Color = this.main,
        variant: Color = this.variant,
        onAccent: Color = this.onAccent,
    ): TextColors = TextColors(
        main = main,
        variant = variant,
        onAccent = onAccent,
    )

}