package com.github.freshmorsikov.moviematcher.feature.favorites.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.favorites.domain.GetFavoriteMovieListUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FavoritesViewModel(
    getFavoriteMovieListUseCase: GetFavoriteMovieListUseCase
) : UdfViewModel<FavoritesUdf.State, FavoritesUdf.Action, FavoritesUdf.Event>(
    initState = { FavoritesUdf.State.Loading }
) {

    init {
        getFavoriteMovieListUseCase().onEach { movieList ->
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

    override suspend fun handleEffects(action: FavoritesUdf.Action) {}

}