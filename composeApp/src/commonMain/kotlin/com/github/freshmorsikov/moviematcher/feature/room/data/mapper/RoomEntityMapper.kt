package com.github.freshmorsikov.moviematcher.feature.room.data.mapper

import com.github.freshmorsikov.moviematcher.feature.room.data.model.RoomEntity
import com.github.freshmorsikov.moviematcher.shared.domain.model.Room

fun RoomEntity.toRoom(): Room {
    return Room(
        id = id,
        code = code,
        genreFilter = genreFilter.orEmpty(),
    )
}
