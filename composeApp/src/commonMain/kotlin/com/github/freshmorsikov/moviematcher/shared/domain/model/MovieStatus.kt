package com.github.freshmorsikov.moviematcher.shared.domain.model

enum class MovieStatus {
    Undefined,
    Liked,
    Disliked;

    companion object {
        fun fromName(status: String): MovieStatus {
            return entries.firstOrNull { movieStatus ->
                movieStatus.name.equals(status, ignoreCase = true)
            } ?: Undefined
        }
    }
}
