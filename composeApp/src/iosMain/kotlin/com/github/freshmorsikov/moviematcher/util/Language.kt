package com.github.freshmorsikov.moviematcher.util

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.preferredLanguages

actual fun getSystemLanguage(): String {
    val preferred = NSLocale.preferredLanguages.firstOrNull() as? String
    return preferred ?: NSLocale.currentLocale.languageCode
}