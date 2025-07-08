package com.github.freshmorsikov.moviematcher.util

fun Double.toRatingFormat(): String {
    return this.toString().take(3)
}