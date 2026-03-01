package com.github.freshmorsikov.moviematcher.app.navigation

import kotlinx.serialization.Serializable

interface NavigationRoute {

    sealed interface BottomNavigationRoute : NavigationRoute

    @Serializable
    data class Entry(
        val code: String? = null,
    ) : NavigationRoute

    @Serializable
    data object NoConnection : NavigationRoute

    @Serializable
    data object Name : NavigationRoute

    @Serializable
    data object Pairing : NavigationRoute

    @Serializable
    data object Swipe : BottomNavigationRoute

    @Serializable
    data object Favorite : BottomNavigationRoute

    @Serializable
    data object Matches : BottomNavigationRoute

    @Serializable
    data class MovieDetails(val movieId: Long) : NavigationRoute

}
