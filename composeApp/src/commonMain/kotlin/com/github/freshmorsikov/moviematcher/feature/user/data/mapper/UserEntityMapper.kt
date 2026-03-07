package com.github.freshmorsikov.moviematcher.feature.user.data.mapper

import com.github.freshmorsikov.moviematcher.feature.user.data.model.UserEntity
import com.github.freshmorsikov.moviematcher.feature.user.domain.User

fun UserEntity.toUser(): User {
    return User(
        id = id,
        room = room,
        name = name,
    )
}
