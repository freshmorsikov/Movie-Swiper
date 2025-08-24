package com.github.freshmorsikov.moviematcher.feature.favorite.domain

import com.github.freshmorsikov.moviematcher.feature.swipe.data.MovieRepository
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.Movie
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.MovieStatus
import kotlinx.coroutines.flow.Flow

class GetFavoriteMovieListUseCase(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(): Flow<List<Movie>> {
        return movieRepository.getMovieListFlowByStatus(status = MovieStatus.Liked)
    }

}