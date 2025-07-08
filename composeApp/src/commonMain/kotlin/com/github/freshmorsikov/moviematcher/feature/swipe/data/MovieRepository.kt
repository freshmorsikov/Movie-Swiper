package com.github.freshmorsikov.moviematcher.feature.swipe.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.github.freshmorsikov.moviematcher.MovieEntity
import com.github.freshmorsikov.moviematcher.MovieEntityQueries
import com.github.freshmorsikov.moviematcher.api.ApiService
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.Movie
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.MovieStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val movieEntityQueries: MovieEntityQueries,
    private val apiService: ApiService,
) {


    private var page: Int = 0

    fun getMovieListFlowByStatus(status: MovieStatus): Flow<List<Movie>> {
        return movieEntityQueries.getMoviesByStatus(status = status.name)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { movieEntityList ->
                movieEntityList.map { movieEntity ->
                    Movie(
                        id = movieEntity.id,
                        title = movieEntity.title,
                        originalTitle = movieEntity.originalTitle,
                        posterPath = movieEntity.posterPath,
                        releaseDate = movieEntity.releaseDate,
                        voteAverage = movieEntity.voteAverage,
                        status = MovieStatus.Undefined.name,
                    )
                }
            }
    }

    fun getMovieCountByStatus(status: MovieStatus): Long {
        return movieEntityQueries.getMovieCountByStatus(status = status.name).executeAsOne()
    }

    suspend fun loadMoreMoviesByStatus() {
        page++
        apiService.getMovieList(page = page)
            .onSuccess { movieResponse ->
                movieResponse.results.onEach { movie ->
                    val movieEntity = MovieEntity(
                        id = movie.id,
                        title = movie.title,
                        originalTitle = movie.originalTitle,
                        posterPath = movie.posterPath,
                        releaseDate = movie.releaseDate,
                        voteAverage = movie.voteAverage,
                        status = MovieStatus.Undefined.name,
                    )
                    movieEntityQueries.insert(movieEntity = movieEntity)
                }
            }
    }

    fun updateMovieStatus(
        id: Long,
        status: MovieStatus
    ) {
        movieEntityQueries.updateMovieStatus(
            id = id,
            status = status.name,
        )
    }


}