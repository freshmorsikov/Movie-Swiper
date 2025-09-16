package com.github.freshmorsikov.moviematcher.feature.swipe.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.Movie

interface SwipeUdf {

    sealed interface State : Udf.State {

        data object Loading : State

        data class Data(
            val movies: List<Movie>
        ) : State

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
        data class FinishSwiping(val movieCardState: MovieCardState.Swiped) : Action
        data class MoreClick(val id: Long) : Action
    }

    sealed interface Event : Udf.Event

}