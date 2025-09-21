package com.github.freshmorsikov.moviematcher.feature.details.domain

import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import kotlinx.coroutines.flow.Flow

class GetMovieFlowByIdUseCase(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(id: Long): Flow<Movie> {
        return movieRepository.getMovieFlowById(id)
    }

}