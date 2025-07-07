package com.github.freshmorsikov.moviematcher

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform