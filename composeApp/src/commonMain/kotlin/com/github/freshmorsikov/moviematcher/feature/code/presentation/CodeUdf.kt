package com.github.freshmorsikov.moviematcher.feature.code.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf

interface CodeUdf {

    data class State(val pairId: String) : Udf.State

    sealed interface Action : Udf.Action {
        data class UpdatePairId(val pairId: String): Action
        data object CloseClick: Action
    }

    sealed interface Event : Udf.Event {
        data object GoBack: Event
    }

}