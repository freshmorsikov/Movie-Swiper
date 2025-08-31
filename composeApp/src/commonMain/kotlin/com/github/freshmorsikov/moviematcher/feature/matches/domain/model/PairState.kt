package com.github.freshmorsikov.moviematcher.feature.matches.domain.model

import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.Movie

sealed interface PairState {

    val code: String

    data class Paired(
        override val code: String,
        val matchedMovieList: List<Movie>,
    ) : PairState

    data class NotPaired(
        override val code: String
    ) : PairState

}