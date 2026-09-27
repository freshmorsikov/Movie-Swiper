package com.github.freshmorsikov.moviematcher.feature.watchlists.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.watchlists.domain.GetMovieWatchlistsFlowUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class WatchlistsViewModel(
    getMovieWatchlistsFlowUseCase: GetMovieWatchlistsFlowUseCase,
) : UdfViewModel<WatchlistsUdf.State, WatchlistsUdf.Action, WatchlistsUdf.Event>(
    initState = { WatchlistsUdf.State.Loading }
) {

    init {
        getMovieWatchlistsFlowUseCase().onEach { watchlists ->
            onAction(
                WatchlistsUdf.Action.UpdateWatchlists(
                    watchlists = watchlists,
                )
            )
        }.launchIn(viewModelScope)
    }

    override fun reduce(action: WatchlistsUdf.Action): WatchlistsUdf.State {
        return when (action) {
            is WatchlistsUdf.Action.UpdateWatchlists -> WatchlistsUdf.State.Data(
                watchlists = action.watchlists,
            )
        }
    }

}
