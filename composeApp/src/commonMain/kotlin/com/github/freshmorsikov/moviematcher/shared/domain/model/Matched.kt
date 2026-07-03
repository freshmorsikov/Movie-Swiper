package com.github.freshmorsikov.moviematcher.shared.domain.model

data class Matched(
    val id: String,
    val room: String,
    val movie: Long,
    val active: Boolean,
)
