package com.github.freshmorsikov.moviematcher.feature.user.data.model

import kotlinx.serialization.Serializable

@Serializable
data class InsertUser(
    val room: String,
    val name: String? = null,
)

@Serializable
data class UserEntity(
    val id: String,
    val room: String,
    val name: String? = null,
)
