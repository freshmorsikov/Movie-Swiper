package com.github.freshmorsikov.moviematcher.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageResponse<T>(
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<T>
)
