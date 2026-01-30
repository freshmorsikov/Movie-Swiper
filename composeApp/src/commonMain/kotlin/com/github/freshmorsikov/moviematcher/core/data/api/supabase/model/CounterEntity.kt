package com.github.freshmorsikov.moviematcher.core.data.api.supabase.model

import kotlinx.serialization.Serializable

@Serializable
data class CounterEntity(
    val id: Int,
    val value: Long,
)