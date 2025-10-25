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
    text: TextColors,
) {

    var primary by mutableStateOf(primary)
        internal set

    var background by mutableStateOf(background)
        internal set

    var surface by mutableStateOf(surface)
        internal set

    var text by mutableStateOf(text)
        internal set


    fun copy(
        primary: Color = this.primary,
        background: Color = this.background,
        surface: Color = this.surface,
        text: TextColors = this.text,
    ): ColorScheme = ColorScheme(
        primary = primary,
        background = background,
        surface = surface,
        text = text.copy(
            main = text.main,
            variant = text.variant,
        ),
    )
}

@Stable
class TextColors(
    main: Color,
    variant: Color,
) {
    var main by mutableStateOf(main)
        internal set

    var variant by mutableStateOf(variant)
        internal set

    fun copy(
        main: Color = this.main,
        variant: Color = this.variant,
    ): TextColors = TextColors(
        main = main,
        variant = variant,
    )

}