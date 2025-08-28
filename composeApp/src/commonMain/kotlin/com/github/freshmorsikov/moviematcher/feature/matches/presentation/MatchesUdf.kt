package com.github.freshmorsikov.moviematcher.feature.matches.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf
import com.github.freshmorsikov.moviematcher.feature.matches.domain.model.PairState

interface MatchesUdf {

    sealed interface State : Udf.State {

        data object Loading : State
        data class Data(val pairState: PairState) : State

    }

    sealed interface Action : Udf.Action {
        data object CreatePairClick : Action
        data object JoinPairClick : Action
        data class UpdatePair(val pairState: PairState) : Action
    }

    sealed interface Event : Udf.Event {
        data object OpenCode : Event
        data object OpenJoinPair : Event
    }

}