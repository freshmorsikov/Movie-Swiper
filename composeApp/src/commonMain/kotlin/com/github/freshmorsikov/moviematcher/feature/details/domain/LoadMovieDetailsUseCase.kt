package com.github.freshmorsikov.moviematcher.feature.details.domain

import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository

class LoadMovieDetailsUseCase(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(movieId: Long) {
        movieRepository.loadMovieDetailsById(id = movieId)
    }

}