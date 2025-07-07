package com.github.freshmorsikov.moviematcher.util

fun Float.toRatingFormat(): String {
    return this.toString().take(3)
}