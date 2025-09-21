package com.github.freshmorsikov.moviematcher.feature.favorites.domain

import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import com.github.freshmorsikov.moviematcher.shared.domain.model.MovieStatus
import kotlinx.coroutines.flow.Flow

class GetFavoriteMovieListUseCase(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(): Flow<List<Movie>> {
        return movieRepository.getMovieListFlowByStatus(status = MovieStatus.Liked)
    }

}