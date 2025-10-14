package com.github.freshmorsikov.moviematcher.feature.matches.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie

interface MatchesUdf {

    sealed interface State : Udf.State {
        data object Loading : State
        data object Empty : State
        data class Data(val movies: List<Movie>) : State

    }

    sealed interface Action : Udf.Action {
        data class UpdateMovies(val movies: List<Movie>) : Action
    }

    sealed interface Event : Udf.Event

}