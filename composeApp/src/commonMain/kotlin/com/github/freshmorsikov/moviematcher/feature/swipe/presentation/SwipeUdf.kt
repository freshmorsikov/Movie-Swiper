package com.github.freshmorsikov.moviematcher.feature.swipe.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie

interface SwipeUdf {

    data class State(
        val pairState: PairState?,
        val movies: List<Movie>?,
    ) : Udf.State

    sealed interface PairState {
        data object NotLinked : PairState
        data object Linking : PairState
        data class Linked(val code: String) : PairState
    }

    sealed interface MovieCardState {
        data object Center : MovieCardState
        sealed interface Swiped : MovieCardState {
            data object Left : Swiped
            data object Right : Swiped
        }
    }

    sealed interface Action : Udf.Action {
        data class UpdateMovie(val movies: List<Movie>) : Action
        data class HandleCode(val code: String?) : Action
        data class SetPairState(val pairState: PairState) : Action
        data class FinishSwiping(val movieCardState: MovieCardState.Swiped) : Action
    }

    sealed interface Event : Udf.Event

}