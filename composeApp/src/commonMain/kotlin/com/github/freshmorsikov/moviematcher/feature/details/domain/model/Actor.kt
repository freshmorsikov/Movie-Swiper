package com.github.freshmorsikov.moviematcher.feature.details.domain.model

data class Actor(
    val id: Long,
    val name: String,
    val character: String,
    val profilePath: String,
    val order: Int,
) {

    companion object {
        val mock = Actor(
            id = 0,
            name = "Valentin Freshmorsikov",
            character = "Developer",
            profilePath = "",
            order = 0,
        )
    }
}