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
    stroke: Color,
    warning: Color,
    error: Color,
    shimmer: ShimmerColors,
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

    var stroke by mutableStateOf(stroke)
        internal set

    var error by mutableStateOf(error)
        internal set

    var warning by mutableStateOf(warning)
        internal set

    var shimmer by mutableStateOf(shimmer)
        internal set

    var text by mutableStateOf(text)
        internal set

    fun copy(
        primary: Color = this.primary,
        background: Color = this.background,
        surface: Color = this.surface,
        icon: Color = this.icon,
        stroke: Color = this.stroke,
        warning: Color = this.warning,
        error: Color = this.error,
        shimmer: ShimmerColors = this.shimmer,
        text: TextColors = this.text,
    ): ColorScheme = ColorScheme(
        primary = primary,
        background = background,
        surface = surface,
        icon = icon,
        stroke = stroke,
        warning = warning,
        error = error,
        shimmer = shimmer.copy(
            container = shimmer.container,
            content = shimmer.content,
        ),
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
    onWarning: Color,
) {

    var main by mutableStateOf(main)
        internal set

    var variant by mutableStateOf(variant)
        internal set

    var onAccent by mutableStateOf(onAccent)
        internal set

    var onWarning by mutableStateOf(onWarning)
        internal set

    fun copy(
        main: Color = this.main,
        variant: Color = this.variant,
        onAccent: Color = this.onAccent,
        onWarning: Color = this.onWarning,
    ): TextColors = TextColors(
        main = main,
        variant = variant,
        onAccent = onAccent,
        onWarning = onWarning,
    )

}

@Stable
class ShimmerColors(
    container: Color,
    content: Color,
) {

    var container by mutableStateOf(container)
        internal set

    var content by mutableStateOf(content)
        internal set

    fun copy(
        container: Color = this.container,
        content: Color = this.content,
    ): ShimmerColors = ShimmerColors(
        container = container,
        content = content,
    )

}