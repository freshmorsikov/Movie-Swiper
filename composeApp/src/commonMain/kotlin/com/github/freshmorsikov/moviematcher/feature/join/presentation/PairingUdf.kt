package com.github.freshmorsikov.moviematcher.feature.join.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf

class PairingUdf {

    sealed interface State: Udf.State {
        data object Loading: State
        data object Success: State
    }
    sealed interface Action: Udf.Action {
        data object ShowSuccess : Action
        data class HandleCode(val code: String?) : Action
    }
    data object Event: Udf.Event

}