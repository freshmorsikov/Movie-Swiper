package com.github.freshmorsikov.moviematcher.feature.favorites.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.watchlists.domain.GetMovieListFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.watchlists.domain.WatchlistType
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FavoritesViewModel(
    watchlistType: WatchlistType,
    getMovieListFlowUseCase: GetMovieListFlowUseCase,
) : UdfViewModel<FavoritesUdf.State, FavoritesUdf.Action, FavoritesUdf.Event>(
    initState = { FavoritesUdf.State.Loading }
) {

    init {
        getMovieListFlowUseCase(watchlistType = watchlistType).onEach { movieList ->
            onAction(FavoritesUdf.Action.UpdateMovieList(movieList = movieList))
        }.launchIn(viewModelScope)
    }

    override fun reduce(action: FavoritesUdf.Action): FavoritesUdf.State {
        return when (action) {
            is FavoritesUdf.Action.UpdateMovieList -> {
                if (action.movieList.isEmpty()) {
                    FavoritesUdf.State.Empty
                } else {
                    FavoritesUdf.State.Data(movieList = action.movieList)
                }
            }
        }
    }

}
