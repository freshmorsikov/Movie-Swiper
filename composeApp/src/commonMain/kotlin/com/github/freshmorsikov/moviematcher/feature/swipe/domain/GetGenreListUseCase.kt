package com.github.freshmorsikov.moviematcher.feature.swipe.domain

import com.github.freshmorsikov.moviematcher.feature.movie.domain.model.Genre
import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository

class GetGenreListUseCase(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(): List<Genre> {
        return movieRepository.getGenreList()
    }
}
