package com.github.freshmorsikov.moviematcher.feature.matches.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf

interface MatchesUdf {

    data class State(
        val pairId: String
    ): Udf.State

    sealed interface Action: Udf.Action {
        data object CreatePairClick: Action
        data object JoinPairClick: Action
        data class UpdatePairId(val pairId: String): Action
    }

    sealed interface Event: Udf.Event {
        data object OpenCode: Event
        data object OpenJoinPair: Event
    }

}