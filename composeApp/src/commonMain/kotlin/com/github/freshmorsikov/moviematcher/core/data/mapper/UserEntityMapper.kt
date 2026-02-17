package com.github.freshmorsikov.moviematcher.core.data.mapper

import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.UserEntity
import com.github.freshmorsikov.moviematcher.shared.domain.model.User

fun UserEntity.toUser(): User {
    return User(
        id = id,
        room = room,
        name = name,
    )
}
