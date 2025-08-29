package com.github.freshmorsikov.moviematcher.feature.favorites.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.Movie

interface FavoritesUdf {

    sealed interface State : Udf.State {

        data object Loading: State
        data object Empty: State
        data class Data(val movieList: List<Movie>): State

    }

    sealed interface Action : Udf.Action {
        data class UpdateMovieList(val movieList: List<Movie>) : Action
    }

    sealed interface Event : Udf.Event

}