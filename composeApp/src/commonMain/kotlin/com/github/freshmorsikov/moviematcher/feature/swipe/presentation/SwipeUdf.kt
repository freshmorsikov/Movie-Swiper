package com.github.freshmorsikov.moviematcher.feature.swipe.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.Movie

interface SwipeUdf {

    sealed interface State : Udf.State {

        data object Loading : State

        data class Data(
            val movies: List<Movie>,
            val swipe: SwipeDirection?,
        ) : State

    }

    enum class SwipeDirection {
        Left,
        Right
    }

    sealed interface Action : Udf.Action {
        data class UpdateMovie(val movies: List<Movie>) : Action
        data object Like : Action
        data object Dislike : Action
        data object FinishSwiping : Action
        data class MoreClick(val id: Long) : Action
    }

    sealed interface Event : Udf.Event

}