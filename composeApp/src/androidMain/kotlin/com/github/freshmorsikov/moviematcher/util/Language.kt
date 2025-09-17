package com.github.freshmorsikov.moviematcher.util

import java.util.Locale

actual fun getSystemLanguage(): String {
    return Locale.getDefault().language
}