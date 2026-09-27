package com.github.freshmorsikov.moviematcher.feature.watchlists.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf
import com.github.freshmorsikov.moviematcher.feature.watchlists.domain.Watchlist

interface WatchlistsUdf {

    sealed interface State : Udf.State {
        data object Loading : State
        data class Data(val watchlists: List<Watchlist>) : State
    }

    sealed interface Action : Udf.Action {
        data class UpdateWatchlists(val watchlists: List<Watchlist>) : Action
    }

    sealed interface Event : Udf.Event

}
