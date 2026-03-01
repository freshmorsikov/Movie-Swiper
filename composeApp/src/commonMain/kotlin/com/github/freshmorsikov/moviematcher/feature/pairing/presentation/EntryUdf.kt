package com.github.freshmorsikov.moviematcher.feature.pairing.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf

interface EntryUdf {

    data object State : Udf.State

    sealed interface Action : Udf.Action {
        data object CheckNext : Action
    }

    sealed interface Event : Udf.Event {
        data object NavigateToNoConnection : Event
        data object NavigateToName : Event
        data object NavigateToSwipe : Event
        data object NavigateToPairing : Event
    }

}
