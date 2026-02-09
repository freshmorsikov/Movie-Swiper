package com.github.freshmorsikov.moviematcher

import android.os.Build

class AndroidPlatform : KmpPlatform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val os: OS = OS.Android
}

actual fun getPlatform(): KmpPlatform = AndroidPlatform()

actual fun isDebug(): Boolean {
    return BuildConfig.DEBUG
}