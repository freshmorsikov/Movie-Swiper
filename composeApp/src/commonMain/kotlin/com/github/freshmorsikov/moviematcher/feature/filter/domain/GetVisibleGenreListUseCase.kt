package com.github.freshmorsikov.moviematcher.feature.filter.domain

class GetVisibleGenreListUseCase {

    operator fun invoke(
        genres: List<SelectableGenre>,
        searchQuery: String,
    ): List<SelectableGenre> {
        val normalizedQuery = searchQuery.trim().lowercase()
        if (normalizedQuery.isEmpty()) {
            return genres
        }

        return genres.filter { genre ->
            genre.genre.name.lowercase().contains(other = normalizedQuery)
        }
    }
}
