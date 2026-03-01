package com.github.freshmorsikov.moviematcher.feature.no_connection.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf

interface NoConnectionUdf {

    data object State : Udf.State

    sealed interface Action : Udf.Action {
        data object Retry : Action
    }

    sealed interface Event : Udf.Event {
        data object NavigateToEntry : Event
    }
}
