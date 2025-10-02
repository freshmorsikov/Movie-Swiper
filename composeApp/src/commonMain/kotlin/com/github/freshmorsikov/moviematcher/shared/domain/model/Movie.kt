package com.github.freshmorsikov.moviematcher.shared.domain.model

data class Movie(
    val id: Long,
    val title: String,
    val originalTitle: String,
    val posterPath: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Long,
    val popularity: Double,
    val status: String,
    val overview: String?,
    val runtime: Long?,
    val budget: Long?,
    val revenue: Long?,
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
            voteCount = 100,
            popularity = 12.34,
            status = "liked",
            genres = listOf("Comedy", "Drama", "Animation"),
            overview = "The Demon Slayer Corps are drawn into the Infinity Castle, where Tanjiro, Nezuko, and the Hashira face terrifying Upper Rank demons in a desperate fight as the final battle against Muzan Kibutsuji begins.",
            runtime = 135,
            budget = 1_800_000,
            revenue = 32_000_000,
        )
    }
}