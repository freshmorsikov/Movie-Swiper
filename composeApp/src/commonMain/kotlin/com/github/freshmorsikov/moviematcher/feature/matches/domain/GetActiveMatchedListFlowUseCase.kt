package com.github.freshmorsikov.moviematcher.feature.matches.domain

import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetActiveMatchedMovieListFlowUseCase(
    private val getMatchedListFlowUseCase: GetMatchedListFlowUseCase,
    private val movieRepository: MovieRepository,
) {

    operator fun invoke(): Flow<List<Movie>> {
        return getMatchedListFlowUseCase()
            .map { matchedList ->
                val activeMovieIds = matchedList
                    .filter { matched -> matched.active }
                    .map { matched -> matched.movie }
                movieRepository.getMoviesByIds(ids = activeMovieIds)
            }
    }

}
