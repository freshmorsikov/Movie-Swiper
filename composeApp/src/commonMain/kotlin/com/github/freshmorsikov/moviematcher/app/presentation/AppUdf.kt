package com.github.freshmorsikov.moviematcher.app.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf

interface AppUdf {

    data class State(
        val newMatches: Int,
        val isCurrentMatches: Boolean,
    ) : Udf.State

    sealed interface Action : Udf.Action {
        data object UpdateNewMatches: Action
        data class UpdateIsCurrentMatches(val isCurrentMatches: Boolean) : Action
    }

    sealed interface Event : Udf.Event {
        data object NewMatchFound: Event
    }

}