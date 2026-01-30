package com.github.freshmorsikov.moviematcher.core.data.api.supabase.model

import kotlinx.serialization.Serializable

@Serializable
data class InsertUser(
    val room: String,
)

@Serializable
data class UserEntity(
    val id: String,
    val room: String,
)