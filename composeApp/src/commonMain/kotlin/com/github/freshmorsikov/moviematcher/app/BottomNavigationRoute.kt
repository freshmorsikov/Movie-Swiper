package com.github.freshmorsikov.moviematcher.app

import kotlinx.serialization.Serializable

sealed interface BottomNavigationRoute: NavigationRoute

interface NavigationRoute {

    @Serializable
    data object Swipe: BottomNavigationRoute

    @Serializable
    data object Favorite: BottomNavigationRoute

    @Serializable
    data object Matches: BottomNavigationRoute

    @Serializable
    data object Code: NavigationRoute

    @Serializable
    data object JoinPair: NavigationRoute

    @Serializable
    data object SuccessfulJoining: NavigationRoute

    @Serializable
    data class MovieDetails(val movieId: Long): NavigationRoute

}