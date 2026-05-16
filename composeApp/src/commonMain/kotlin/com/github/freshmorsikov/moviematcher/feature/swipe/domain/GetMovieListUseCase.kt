package com.github.freshmorsikov.moviematcher.feature.swipe.domain

import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository
import com.github.freshmorsikov.moviematcher.shared.domain.GetRoomFlowCaseCase
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import com.github.freshmorsikov.moviematcher.shared.domain.model.MovieStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

private const val LOAD_MOVIE_LIMIT = 5

class GetMovieListUseCase(
    private val movieRepository: MovieRepository,
    private val getRoomFlowCaseCase: GetRoomFlowCaseCase,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Movie>> {
        return getRoomFlowCaseCase()
            .map { room ->
                room.genreFilter
            }
            .distinctUntilChanged()
            .flatMapLatest { genreFilter ->
                movieRepository.getMovieListFlow(
                    status = MovieStatus.Undefined,
                    genreFilter = genreFilter,
                ).transform { movieList ->
                    emit(movieList)
                    if (movieList.size < LOAD_MOVIE_LIMIT) {
                        loadMovies(genreFilter = genreFilter)
                    }
                }
            }
    }

    private suspend fun loadMovies(genreFilter: List<Long>) {
        val movieCount = movieRepository.getMovieCountByStatusAndGenreFilter(
            status = MovieStatus.Undefined,
            genreFilter = genreFilter,
        )
        if (movieCount < LOAD_MOVIE_LIMIT) {
            movieRepository.loadMoreMoviesByStatus(genreFilter = genreFilter)
        }
    }

}
