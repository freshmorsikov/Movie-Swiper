package com.github.freshmorsikov.moviematcher.feature.join.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf

interface JoinPairUdf {

    data class State(val code: String) : Udf.State {

        val saveButtonEnabled: Boolean = code.length == 4

    }

    sealed interface Action : Udf.Action {
        data object CloseClick : Action
        data class UpdateCode(val input: String) : Action
        data object SaveCode : Action
    }

    sealed interface Event : Udf.Event {
        data object GoBack : Event
        data object OpenSuccess : Event
    }

}