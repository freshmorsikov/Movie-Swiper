package com.github.freshmorsikov.moviematcher.feature.movie.data.mapper

import com.github.freshmorsikov.moviematcher.GenreEntity
import com.github.freshmorsikov.moviematcher.core.data.api.model.GenreResponse
import com.github.freshmorsikov.moviematcher.feature.movie.domain.model.Genre

fun GenreResponse.toGenreEntity(): GenreEntity {
    return GenreEntity(
        id = id,
        genreName = name,
    )
}

fun GenreEntity.toGenre(): Genre {
    return Genre(
        id = id,
        name = genreName,
    )
}

fun GenreResponse.toGenre(): Genre {
    return Genre(
        id = id,
        name = name,
    )
}
