package com.github.freshmorsikov.moviematcher.feature.details.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie

interface MovieDetailsUdf {
    sealed interface State : Udf.State {
        data object Loading : State
        data class Data(
            val movie: Movie,
            val trailer: String? = null,
        ) : State
    }

    sealed interface Action : Udf.Action {
        data class UpdateMovie(val movie: Movie): Action
    }
    data object Event : Udf.Event
}