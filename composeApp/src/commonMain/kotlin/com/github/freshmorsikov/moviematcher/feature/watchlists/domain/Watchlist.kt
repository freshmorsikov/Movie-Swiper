package com.github.freshmorsikov.moviematcher.feature.watchlists.domain

import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie

data class Watchlist(
    val type: WatchlistType,
    val movieCount: Int,
    val previewMovies: List<Movie>,
)
