package com.github.freshmorsikov.moviematcher.feature.matches.domain.model

import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.Movie

sealed interface PairState {

    data class Paired(
        val code: String,
        val matchedMovieList: List<Movie>,
    ): PairState
    data object NotPaired: PairState

}