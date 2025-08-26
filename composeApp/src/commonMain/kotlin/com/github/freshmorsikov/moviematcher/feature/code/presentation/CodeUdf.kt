package com.github.freshmorsikov.moviematcher.feature.code.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf

interface CodeUdf {

    data class State(val pairId: String) : Udf.State

    sealed interface Action : Udf.Action {
        data class UpdatePairId(val pairId: String): Action
        data object CloseClick: Action
        data object CopyClick: Action
    }

    sealed interface Event : Udf.Event {
        data object GoBack: Event
        data class SaveToClipboard(val pairId: String): Event
    }

}