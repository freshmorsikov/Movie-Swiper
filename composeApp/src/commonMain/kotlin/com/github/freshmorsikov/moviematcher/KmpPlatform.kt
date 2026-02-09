package com.github.freshmorsikov.moviematcher

enum class OS {
    Android,
    iOS;
}

interface KmpPlatform {
    val name: String
    val os: OS
}

expect fun getPlatform(): KmpPlatform

expect fun isDebug(): Boolean