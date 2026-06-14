package com.github.freshmorsikov.moviematcher.feature.watchlists.domain

import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetMatchedListFlowUseCase
import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import com.github.freshmorsikov.moviematcher.shared.domain.model.MovieStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlin.collections.List
import kotlin.collections.emptyList
import kotlin.collections.listOf
import kotlin.collections.take

private const val PREVIEW_MOVIE_LIMIT = 3

class GetMovieWatchlistsFlowUseCase(
    private val movieRepository: MovieRepository,
    private val getMatchedListFlowUseCase: GetMatchedListFlowUseCase,
) {

    operator fun invoke(): Flow<List<Watchlist>> {
        val likedMovies = movieRepository.getMovieListFlow(status = MovieStatus.Liked)
            .onStart { emit(emptyList()) }
        val dislikedMovies = movieRepository.getMovieListFlow(status = MovieStatus.Disliked)
            .onStart { emit(emptyList()) }
        val matchedMovies = getMatchedListFlowUseCase()
            .onStart { emit(emptyList()) }

        return combine(
            likedMovies,
            dislikedMovies,
            matchedMovies,
        ) { liked, disliked, matches ->
            listOf(
                liked.toWatchlist(type = WatchlistType.Liked),
                disliked.toWatchlist(type = WatchlistType.Disliked),
                matches.toWatchlist(type = WatchlistType.Matches),
                Watchlist(
                    type = WatchlistType.Watched,
                    movieCount = 0,
                    previewMovies = emptyList(),
                ),
            )
        }
    }

    private fun List<Movie>.toWatchlist(type: WatchlistType): Watchlist {
        return Watchlist(
            type = type,
            movieCount = size,
            previewMovies = take(PREVIEW_MOVIE_LIMIT),
        )
    }

}
