package com.github.freshmorsikov.moviematcher.util

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry

actual fun clipEntryOf(string: String): ClipEntry {
    return ClipEntry(ClipData.newPlainText(string, string))
}