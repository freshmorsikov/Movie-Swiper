package com.github.freshmorsikov.moviematcher.feature.pairing.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf

class PairingUdf {

    sealed interface State: Udf.State {
        data object Loading: State
        data class Result(val isSuccess: Boolean): State
    }
    sealed interface Action: Udf.Action {
        data class ShowResult(val isSuccess: Boolean) : Action
        data class HandleCode(val code: String?) : Action
    }
    data object Event: Udf.Event

}