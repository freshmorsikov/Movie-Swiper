package com.github.freshmorsikov.moviematcher.core.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

val WindowInsets.Companion.none
    get() = WindowInsets(0, 0, 0, 0)

@Composable
fun paddingWithSystemTopBar(all: Dp): PaddingValues {
    val windowInsets = WindowInsets.systemBars

    return PaddingValues(
        start = all,
        top = all + windowInsets.asPaddingValues().calculateTopPadding(),
        end = all,
        bottom = all,
    )
}