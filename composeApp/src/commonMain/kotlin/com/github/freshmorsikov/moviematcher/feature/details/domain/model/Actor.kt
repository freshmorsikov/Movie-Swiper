package com.github.freshmorsikov.moviematcher.feature.details.domain.model

data class Actor(
    val id: Long,
    val name: String,
    val character: String,
    val profilePath: String,
    val order: Int,
)