package com.github.freshmorsikov.moviematcher.feature.swipe.domain

import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository

class LoadGenreListUseCase(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke() {
        movieRepository.loadGenreList()
    }
}