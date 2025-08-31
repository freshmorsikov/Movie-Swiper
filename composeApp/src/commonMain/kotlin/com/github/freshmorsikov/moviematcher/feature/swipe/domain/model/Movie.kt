package com.github.freshmorsikov.moviematcher.feature.swipe.domain.model

data class Movie(
    val id: Long,
    val title: String,
    val originalTitle: String,
    val posterPath: String,
    val releaseDate: String,
    val voteAverage: Double,
    val popularity: Double,
    val status: String,
    val genres: List<String>,
) {

    companion object {
        val mock = Movie(
            id = 1,
            title = "Movie",
            originalTitle = "Original movie",
            posterPath = "/path",
            releaseDate = "2025-01-01",
            voteAverage = 9.87,
            popularity = 12.34,
            status = "liked",
            genres = listOf("Comedy", "Drama", "Animation"),
        )
    }
}