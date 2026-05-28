package com.github.freshmorsikov.moviematcher.feature.filter.domain

import com.github.freshmorsikov.moviematcher.feature.movie.domain.model.Genre

data class SelectableGenre(
    val genre: Genre,
    val isSelected: Boolean,
)
