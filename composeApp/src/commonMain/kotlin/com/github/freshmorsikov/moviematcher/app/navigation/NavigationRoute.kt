package com.github.freshmorsikov.moviematcher.app.navigation

import kotlinx.serialization.Serializable

interface NavigationRoute {

    sealed interface BottomNavigationRoute : NavigationRoute

    @Serializable
    data class Name(
        val pairingCode: String?,
    ) : NavigationRoute

    @Serializable
    data class PairingEntry(
        val code: String? = null,
    ) : NavigationRoute

    @Serializable
    data object NoConnection : NavigationRoute

    @Serializable
    data object Swipe : BottomNavigationRoute

    @Serializable
    data object Favorite : BottomNavigationRoute

    @Serializable
    data object Matches : BottomNavigationRoute

    @Serializable
    data class Pairing(
        val code: String? = null
    ) : NavigationRoute

    @Serializable
    data class MovieDetails(val movieId: Long) : NavigationRoute

}
