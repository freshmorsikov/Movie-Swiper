package com.github.freshmorsikov.moviematcher.feature.swipe.domain.model

data class Movie(
    val id: Long,
    val title: String,
    val originalTitle: String,
    val posterPath: String,
    val releaseDate: String,
    val voteAverage: Double,
    val status: String,
)