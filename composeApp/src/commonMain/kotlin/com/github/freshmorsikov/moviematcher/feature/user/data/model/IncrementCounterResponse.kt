package com.github.freshmorsikov.moviematcher.feature.user.data.model

import kotlinx.serialization.Serializable

@Serializable
data class IncrementCounterResponse(
    val value: Long,
)