package com.github.freshmorsikov.moviematcher.util

import androidx.compose.ui.platform.ClipEntry

actual fun clipEntryOf(string: String): ClipEntry {
    return ClipEntry(string)
}