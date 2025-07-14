package com.github.freshmorsikov.moviematcher.feature.swipe.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.Movie

interface SwipeUdf {

    data class State(
        val movieList: List<Movie>,
        val swipingMovieId: SwipingMovieId?
    ) : Udf.State

    sealed interface SwipingMovieId {
        val id: Long

        data class Left(override val id: Long): SwipingMovieId
        data class Right(override val id: Long): SwipingMovieId
    }

    sealed interface Action : Udf.Action {
        data class UpdateMovieList(val movieList: List<Movie>) : Action
        data object Like : Action
        data object Dislike : Action
        data object FinishRemoving : Action
        data class MoreClick(val id: Long) : Action
    }

    sealed interface Event : Udf.Event

}