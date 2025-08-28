package com.github.freshmorsikov.moviematcher.feature.matches.domain.model

sealed interface PairState {

    data class Paired(val code: String): PairState
    data object NotPaired: PairState

}