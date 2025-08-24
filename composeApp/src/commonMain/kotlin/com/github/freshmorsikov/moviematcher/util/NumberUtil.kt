package com.github.freshmorsikov.moviematcher.util

import kotlin.math.pow

fun Double.toRatingFormat(): String {
    return this.toString().take(3)
}

fun Int.pow(x: Int): Int {
    return this.toDouble().pow(x).toInt()
}