package com.github.freshmorsikov.moviematcher.feature.movielist.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.watchlists.domain.GetMovieListFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.watchlists.domain.WatchlistType
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MovieListViewModel(
    watchlistType: WatchlistType,
    getMovieListFlowUseCase: GetMovieListFlowUseCase,
) : UdfViewModel<MovieListUdf.State, MovieListUdf.Action, MovieListUdf.Event>(
    initState = { MovieListUdf.State.Loading }
) {

    init {
        getMovieListFlowUseCase(watchlistType = watchlistType).onEach { movieList ->
            onAction(MovieListUdf.Action.UpdateMovieList(movieList = movieList))
        }.launchIn(viewModelScope)
    }

    override fun reduce(action: MovieListUdf.Action): MovieListUdf.State {
        return when (action) {
            is MovieListUdf.Action.UpdateMovieList -> {
                if (action.movieList.isEmpty()) {
                    MovieListUdf.State.Empty
                } else {
                    MovieListUdf.State.Data(movieList = action.movieList)
                }
            }
        }
    }

}
