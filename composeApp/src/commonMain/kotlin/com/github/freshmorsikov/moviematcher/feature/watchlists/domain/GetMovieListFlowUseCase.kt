package com.github.freshmorsikov.moviematcher.feature.watchlists.domain

import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetMatchedListFlowUseCase
import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import com.github.freshmorsikov.moviematcher.shared.domain.model.MovieStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetMovieListFlowUseCase(
    private val movieRepository: MovieRepository,
    private val getMatchedListFlowUseCase: GetMatchedListFlowUseCase,
) {

    operator fun invoke(watchlistType: WatchlistType): Flow<List<Movie>> {
        return when (watchlistType) {
            WatchlistType.Liked -> movieRepository.getMovieListFlow(status = MovieStatus.Liked)
            WatchlistType.Disliked -> movieRepository.getMovieListFlow(status = MovieStatus.Disliked)
            WatchlistType.Matches -> getMatchedListFlowUseCase()
            WatchlistType.Watched -> flowOf(emptyList())
        }
    }

}
