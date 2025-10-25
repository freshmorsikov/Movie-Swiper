package com.github.freshmorsikov.moviematcher

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val os: OS = OS.Android
}

actual fun getPlatform(): Platform = AndroidPlatform()