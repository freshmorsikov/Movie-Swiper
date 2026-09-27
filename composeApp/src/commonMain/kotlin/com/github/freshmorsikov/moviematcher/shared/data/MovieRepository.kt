package com.github.freshmorsikov.moviematcher.shared.data

import app.cash.sqldelight.Query
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
import com.github.freshmorsikov.moviematcher.core.data.api.TheMovieDbApiService
import com.github.freshmorsikov.moviematcher.core.data.api.model.GenreResponse
import com.github.freshmorsikov.moviematcher.core.data.local.KeyValueStore
import com.github.freshmorsikov.moviematcher.feature.movie.data.mapper.toGenre
import com.github.freshmorsikov.moviematcher.feature.movie.data.mapper.toGenreEntity
import com.github.freshmorsikov.moviematcher.feature.movie.domain.model.Genre
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
private const val PAGE_KEY_SEPARATOR = "_"

class MovieRepository(
    private val movieEntityQueries: MovieEntityQueries,
    private val genreEntityQueries: GenreEntityQueries,
    private val movieWithGenreViewQueries: MovieWithGenreViewQueries,
    private val movieGenreReferenceQueries: MovieGenreReferenceQueries,
    private val keyValueStore: KeyValueStore,
    private val theMovieDbApiService: TheMovieDbApiService,
    private val analyticsManager: AnalyticsManager,
) {

    suspend fun getGenreList(): List<Genre> {
        val localGenreList = getLocalGenreList()
        if (localGenreList.isNotEmpty()) {
            return localGenreList
        }

        return getRemoteGenreList()
    }

    private fun getLocalGenreList(): List<Genre> {
        return genreEntityQueries.getGenreList()
            .executeAsList()
            .map(GenreEntity::toGenre)
    }

    private suspend fun getRemoteGenreList(): List<Genre> {
        var remoteGenreList: List<Genre>? = null
        theMovieDbApiService.getGenreList()
            .onSuccess { genreList ->
                remoteGenreList = genreList.genres.map(GenreResponse::toGenre)
                genreList.genres.forEach { genre ->
                    genreEntityQueries.insert(genre.toGenreEntity())
                }
            }

        return remoteGenreList.orEmpty()
    }

    suspend fun loadMovieDetailsById(id: Long) {
        theMovieDbApiService.getMovieDetailsById(movieId = id)
            .onSuccess { movieDetails ->
                movieEntityQueries.updateMovieDetails(
                    voteAverage = movieDetails.voteAverage,
                    voteCount = movieDetails.voteCount,
                    popularity = movieDetails.popularity,
                    overview = movieDetails.overview,
                    runtime = movieDetails.runtime,
                    budget = movieDetails.budget,
                    revenue = movieDetails.revenue,
                    id = id,
                )
            }
    }

    fun getMovieListFlow(
        status: MovieStatus,
        genreFilter: List<Long> = emptyList(),
    ): Flow<List<Movie>> {
        val query = if (genreFilter.isEmpty()) {
            movieWithGenreViewQueries.getMoviesWithGenreByStatus(status = status.name)
        } else {
            movieWithGenreViewQueries.getMoviesWithGenreByStatusAndGenreIds(
                status = status.name,
                genreIds = genreFilter,
            )
        }

        return getMovieListFlowByQuery(query = query)
    }

    private fun getMovieListFlowByQuery(query: Query<MovieWithGenreView>): Flow<List<Movie>> {
        return query
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { movieWithGenreList ->
                movieWithGenreList.toMovieList()
            }
    }

    fun getMovieCountByStatus(status: MovieStatus): Long {
        return movieEntityQueries.getMovieCountByStatus(status = status.name).executeAsOne()
    }

    fun getMovieCountByStatusAndGenreFilter(
        status: MovieStatus,
        genreFilter: List<Long>,
    ): Long {
        if (genreFilter.isEmpty()) {
            return getMovieCountByStatus(status = status)
        }

        return movieEntityQueries.getMovieCountByStatusAndGenreIds(
            status = status.name,
            genreIds = genreFilter,
        ).executeAsOne()
    }

    @OptIn(ExperimentalTime::class)
    suspend fun loadMoreMoviesByStatus(genreFilter: List<Long>) {
        val pageKey = genreFilter.toPageKey()
        val page = keyValueStore.getInt(pageKey)?.let { cachedPage ->
            cachedPage + 1
        } ?: 1
        theMovieDbApiService.getMovieList(
            page = page,
            genreFilter = genreFilter,
        ).onSuccess { movieResponse ->
            if (page == 1) {
                analyticsManager.sendEvent(event = FetchMoviesEvent)
            }
            keyValueStore.putInt(pageKey, page)
            movieResponse.results.onEach { movie ->
                val movieEntity = MovieEntity(
                    id = movie.id,
                    title = movie.title,
                    originalTitle = movie.originalTitle,
                    posterPath = movie.posterPath,
                    releaseDate = movie.releaseDate,
                    voteAverage = movie.voteAverage,
                    voteCount = movie.voteCount.toLong(),
                    popularity = movie.popularity,
                    overview = null,
                    runtime = null,
                    budget = null,
                    revenue = null,
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

    fun getMovieFlowById(id: Long): Flow<Movie> {
        return movieWithGenreViewQueries.getMovieWithGenreById(id = id)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list ->
                list.toMovie()
            }
    }

    private fun List<MovieWithGenreView>.toMovieList(): List<Movie> {
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
                voteCount = movie.voteCount,
                popularity = movie.popularity,
                status = MovieStatus.fromName(status = movie.status),
                genres = movieWithGenreList.map { it.genreName },
                overview = movie.overview,
                runtime = movie.runtime,
                budget = movie.budget,
                revenue = movie.revenue,
            )
        }
    }

    private fun List<MovieWithGenreView>.toMovie(): Movie {
        val movie = first()
        return Movie(
            id = movie.id,
            title = movie.title,
            originalTitle = movie.originalTitle,
            posterPath = movie.posterPath,
            releaseDate = movie.releaseDate,
            voteAverage = movie.voteAverage,
            voteCount = movie.voteCount,
            popularity = movie.popularity,
            status = MovieStatus.fromName(status = movie.status),
            genres = map { it.genreName },
            overview = movie.overview,
            runtime = movie.runtime,
            budget = movie.budget,
            revenue = movie.revenue,
        )
    }

}

private fun List<Long>.toPageKey(): String {
    if (isEmpty()) {
        return PAGE_KEY
    }

    return PAGE_KEY + PAGE_KEY_SEPARATOR + joinToString(separator = PAGE_KEY_SEPARATOR)
}
