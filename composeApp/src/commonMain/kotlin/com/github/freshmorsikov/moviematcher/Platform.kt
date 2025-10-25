package com.github.freshmorsikov.moviematcher

enum class OS {
    Android,
    iOS;
}

interface Platform {
    val name: String
    val os: OS
}

expect fun getPlatform(): Platform