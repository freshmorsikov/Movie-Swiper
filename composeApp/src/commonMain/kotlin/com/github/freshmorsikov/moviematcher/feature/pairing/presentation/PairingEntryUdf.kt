package com.github.freshmorsikov.moviematcher.feature.pairing.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf

interface PairingEntryUdf {

    data object State : Udf.State

    sealed interface Action : Udf.Action {
        data object CheckUser : Action
    }

    sealed interface Event : Udf.Event {
        data class NavigateToName(val code: String?) : Event
        data class NavigateToPairing(val code: String?) : Event
    }

}
