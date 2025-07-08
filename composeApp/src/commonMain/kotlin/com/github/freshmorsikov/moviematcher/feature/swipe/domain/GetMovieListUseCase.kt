package com.github.freshmorsikov.moviematcher.feature.swipe.domain

import com.github.freshmorsikov.moviematcher.feature.swipe.data.MovieRepository
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.Movie
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.MovieStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val LOAD_MOVIE_LIMIT = 5

class GetMovieListUseCase(
    private val movieRepository: MovieRepository
) {

    private val mutex = Mutex()
    private val scope: CoroutineScope = CoroutineScope(Job())

    operator fun invoke(): Flow<List<Movie>> {
        return movieRepository.getMovieListFlowByStatus(status = MovieStatus.Undefined)
            .onEach { movieList ->
                scope.launch {
                    if (movieList.size < LOAD_MOVIE_LIMIT) {
                        loadMovies()
                    }
                }
            }
    }

    private suspend fun loadMovies() {
        mutex.withLock {
            val movieCount = movieRepository.getMovieCountByStatus(status = MovieStatus.Undefined)
            if (movieCount < LOAD_MOVIE_LIMIT) {
                movieRepository.loadMoreMoviesByStatus()
            }
        }
    }

}