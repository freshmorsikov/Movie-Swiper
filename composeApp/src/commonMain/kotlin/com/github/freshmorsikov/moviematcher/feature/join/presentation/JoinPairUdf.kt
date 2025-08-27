package com.github.freshmorsikov.moviematcher.feature.join.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf

interface JoinPairUdf {

    data object State : Udf.State

    sealed interface Action : Udf.Action {
        data object CloseClick : Action
        data class SaveCode(val code: String) : Action
    }

    sealed interface Event : Udf.Event {
        data object GoBack : Event
    }

}