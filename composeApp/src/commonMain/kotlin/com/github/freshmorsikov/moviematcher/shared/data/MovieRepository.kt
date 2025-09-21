package com.github.freshmorsikov.moviematcher.shared.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.github.freshmorsikov.moviematcher.GenreEntity
import com.github.freshmorsikov.moviematcher.GenreEntityQueries
import com.github.freshmorsikov.moviematcher.MovieEntity
import com.github.freshmorsikov.moviematcher.MovieEntityQueries
import com.github.freshmorsikov.moviematcher.MovieGenreReference
import com.github.freshmorsikov.moviematcher.MovieGenreReferenceQueries
import com.github.freshmorsikov.moviematcher.MovieWithGenreView
import com.github.freshmorsikov.moviematcher.MovieWithGenreViewQueries
import com.github.freshmorsikov.moviematcher.core.analytics.AnalyticsManager
import com.github.freshmorsikov.moviematcher.core.data.api.ApiService
import com.github.freshmorsikov.moviematcher.core.data.local.KeyValueStore
import com.github.freshmorsikov.moviematcher.feature.swipe.analytics.FetchMoviesEvent
import com.github.freshmorsikov.moviematcher.feature.swipe.analytics.FetchMoviesFailedEvent
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import com.github.freshmorsikov.moviematcher.shared.domain.model.MovieStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val PAGE_KEY = "PAGE_KEY"

class MovieRepository(
    private val movieEntityQueries: MovieEntityQueries,
    private val genreEntityQueries: GenreEntityQueries,
    private val movieWithGenreViewQueries: MovieWithGenreViewQueries,
    private val movieGenreReferenceQueries: MovieGenreReferenceQueries,
    private val keyValueStore: KeyValueStore,
    private val apiService: ApiService,
    private val analyticsManager: AnalyticsManager,
) {

    suspend fun loadGenreList() {
        apiService.getGenreList()
            .onSuccess { genreList ->
                genreList.genres.forEach { genre ->
                    val genreEntity = GenreEntity(
                        id = genre.id,
                        genreName = genre.name,
                    )
                    genreEntityQueries.insert(genreEntity)
                }
            }
    }

    fun getMovieListFlowByStatus(status: MovieStatus): Flow<List<Movie>> {
        return movieWithGenreViewQueries.getMoviesWithGenreByStatus(status = status.name)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { movieWithGenreList ->
                movieWithGenreList.toMovieList()
            }
    }

    fun getMovieCountByStatus(status: MovieStatus): Long {
        return movieEntityQueries.getMovieCountByStatus(status = status.name).executeAsOne()
    }

    @OptIn(ExperimentalTime::class)
    suspend fun loadMoreMoviesByStatus() {
        val page = keyValueStore.getInt(PAGE_KEY)?.let { cachedPage ->
            cachedPage + 1
        } ?: 1
        apiService.getMovieList(page = page)
            .onSuccess { movieResponse ->
                if (page == 1) {
                    analyticsManager.sendEvent(event = FetchMoviesEvent)
                }
                keyValueStore.putInt(PAGE_KEY, page)
                movieResponse.results.onEach { movie ->
                    val movieEntity = MovieEntity(
                        id = movie.id,
                        title = movie.title,
                        originalTitle = movie.originalTitle,
                        posterPath = movie.posterPath,
                        releaseDate = movie.releaseDate,
                        voteAverage = movie.voteAverage,
                        popularity = movie.popularity,
                        status = MovieStatus.Undefined.name,
                        uploadTimestamp = Clock.System.now().epochSeconds
                    )
                    movieEntityQueries.insert(movieEntity = movieEntity)
                    movie.genreIds.onEach { genreId ->
                        val movieGenreReference = MovieGenreReference(
                            movieReference = movie.id,
                            genreReference = genreId
                        )
                        movieGenreReferenceQueries.insert(movieGenreReference = movieGenreReference)
                    }
                }
            }.onFailure {
                if (page == 1) {
                    analyticsManager.sendEvent(event = FetchMoviesFailedEvent)
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

    fun getMoviesByIds(ids: List<Long>): List<Movie> {
        return movieWithGenreViewQueries.getMoviesWithGenreByIds(ids = ids)
            .executeAsList()
            .toMovieList()
    }

    fun List<MovieWithGenreView>.toMovieList(): List<Movie> {
        return groupBy { movieWithGenres ->
            movieWithGenres.id
        }.map { (_, movieWithGenreList) ->
            val movie = movieWithGenreList.first()
            Movie(
                id = movie.id,
                title = movie.title,
                originalTitle = movie.originalTitle,
                posterPath = movie.posterPath,
                releaseDate = movie.releaseDate,
                voteAverage = movie.voteAverage,
                voteCount = 0,
                popularity = movie.popularity,
                status = MovieStatus.Undefined.name,
                genres = movieWithGenreList.map { it.genreName },
                overview = null,
                runtime = null,
                budget = null,
                revenue = null,
            )
        }
    }


}