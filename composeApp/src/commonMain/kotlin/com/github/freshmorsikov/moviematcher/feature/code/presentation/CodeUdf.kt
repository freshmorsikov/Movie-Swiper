package com.github.freshmorsikov.moviematcher.feature.code.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf

interface CodeUdf {

    data class State(val code: String) : Udf.State

    sealed interface Action : Udf.Action {
        data class UpdatePairId(val code: String): Action
        data object CloseClick: Action
        data object CopyClick: Action
        data object ShareClick: Action
    }

    sealed interface Event : Udf.Event {
        data object GoBack: Event
        data class SaveToClipboard(val code: String): Event
        data class ShowSharingDialog(val code: String): Event
    }

}