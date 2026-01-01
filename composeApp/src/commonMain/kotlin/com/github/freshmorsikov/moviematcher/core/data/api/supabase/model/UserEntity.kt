package com.github.freshmorsikov.moviematcher.core.data.api.supabase.model

import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val id: Int,
    val room: String,
)