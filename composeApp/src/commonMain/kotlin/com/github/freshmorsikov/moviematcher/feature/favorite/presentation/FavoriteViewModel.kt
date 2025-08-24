package com.github.freshmorsikov.moviematcher.feature.favorite.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.favorite.domain.GetFavoriteMovieListUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FavoriteViewModel(
    getFavoriteMovieListUseCase: GetFavoriteMovieListUseCase
) : UdfViewModel<FavoriteUdf.State, FavoriteUdf.Action, FavoriteUdf.Event>(
    initState = { FavoriteUdf.State.Loading }
) {

    init {
        getFavoriteMovieListUseCase().onEach { movieList ->
            onAction(FavoriteUdf.Action.UpdateMovieList(movieList = movieList.reversed()))
        }.launchIn(viewModelScope)
    }

    override fun reduce(action: FavoriteUdf.Action): FavoriteUdf.State {
        return when (action) {
            is FavoriteUdf.Action.UpdateMovieList -> {
                if (action.movieList.isEmpty()) {
                    FavoriteUdf.State.Empty
                } else {
                    FavoriteUdf.State.Data(movieList = action.movieList)
                }
            }
        }
    }

    override suspend fun handleEffects(action: FavoriteUdf.Action) {}

}